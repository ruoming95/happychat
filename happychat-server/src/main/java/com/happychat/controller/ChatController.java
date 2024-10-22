package com.happychat.controller;

import com.happychat.annotation.GlobalInterceptor;
import com.happychat.config.AppConfig;
import com.happychat.constants.Constants;
import com.happychat.entity.dto.MessageSendDto;
import com.happychat.entity.dto.TokenUserInfoDto;
import com.happychat.entity.pojo.ChatMessage;
import com.happychat.entity.vo.ResponseVo;
import com.happychat.enums.MessageTypeEnum;
import com.happychat.enums.ResponseCodeEnum;
import com.happychat.exception.BusinessException;
import com.happychat.service.ChatMessageService;
import com.happychat.utils.StringTools;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

@RestController("chatController")
@RequestMapping("/chat")
@Validated
public class ChatController extends ABaseController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private AppConfig appConfig;


    @RequestMapping("/sendMessage")
    @GlobalInterceptor
    public ResponseVo sendMessage(HttpServletRequest request,
                                  @NotEmpty String contactId,
                                  @NotEmpty @Length(max = 500) String messageContent,
                                  @NotNull Integer messageType,
                                  Long fileSize,
                                  String fileName,
                                  Integer fileType) {
        MessageTypeEnum type = MessageTypeEnum.getByType(messageType);
        if (null == type || !ArrayUtils.contains(new Integer[]{MessageTypeEnum.CHAT.getType(),
                MessageTypeEnum.MEDIA_CHAT.getType()}, type.getType())) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        TokenUserInfoDto tokenUserInfo = getTokenUserInfo(request);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessageType(messageType);
        logger.info(contactId);
        chatMessage.setContactId(contactId);
        chatMessage.setMessageContent(messageContent);
        chatMessage.setFileName(fileName);
        chatMessage.setFileSize(fileSize);
        chatMessage.setFileType(fileType);
        MessageSendDto sendDto = chatMessageService.saveChatMessage(chatMessage, tokenUserInfo);
        return getSuccessResponseVo(sendDto);
    }

    @RequestMapping("/uploadFile")
//    @GlobalInterceptor
    public ResponseVo uploadFile(HttpServletRequest request,
                                 @NotNull Long messageId,
                                 @RequestParam("file") @NotNull MultipartFile file,
                                 MultipartFile cover) {
        TokenUserInfoDto tokenUserInfo = getTokenUserInfo(request);
        chatMessageService.saveMessageFile(tokenUserInfo.getUserId(), messageId, file, cover);
        return getSuccessResponseVo(null);
    }

    @RequestMapping("/downloadFile")
    @GlobalInterceptor
    public void downloadFile(HttpServletRequest request,
                             HttpServletResponse response,
                             @NotEmpty String fileId,
                             Boolean showCover
    ) {
        TokenUserInfoDto tokenUserInfo = getTokenUserInfo(request);
        OutputStream out = null;
        FileInputStream in = null;
        //判断是不是要下载头像
        try {
            File file = null;
            if (!StringTools.isNumeric(fileId)) {
                String avatarSavePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_AVATAR;
                String avatarPath = avatarSavePath + fileId + Constants.IMG_SUFFIX;
                if (showCover) {
                    avatarPath += Constants.COVER_IMG_SUFFIX;
                }
                file = new File(avatarPath);
                if (!file.exists()) {
                    throw new BusinessException(ResponseCodeEnum.CODE_602);
                }
            } else {
                file = chatMessageService.downloadFile(tokenUserInfo, Long.valueOf(fileId), showCover);
            }

            response.setHeader("Content-Disposition", "attachment;");
            response.setContentType("application/x-msdownload;charset=UTF-8");
            response.setContentLengthLong(file.length());
            out = response.getOutputStream();
            in = new FileInputStream(file);
            int len;
            byte[] buffer = new byte[1024];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            logger.error("文件上传失败", e);
            throw new BusinessException(ResponseCodeEnum.CODE_500);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {

            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {

            }
        }
    }
}

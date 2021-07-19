package kr.devflix.controller;

import com.google.common.collect.ImmutableMap;
import kr.devflix.constant.RoleType;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static kr.devflix.utils.ApiUtils.ApiResult;
import static kr.devflix.utils.ApiUtils.success;

@RestController
@RequestMapping("/a/images")
public class ImageRestController {

    private final String IMAGE_ROOT_DIR_PATH;
    private final Logger logger = LoggerFactory.getLogger(ImageRestController.class);

    public ImageRestController(Environment environment) {
        if (StringUtils.isBlank(environment.getProperty("image.root-drectory"))) {
            IMAGE_ROOT_DIR_PATH = "images/";
        } else {
            IMAGE_ROOT_DIR_PATH = environment.getProperty("image.root-drectory");
        }
    }

    @Secured(RoleType.USER)
    @PostMapping
    public ApiResult<List<ImmutableMap>> actionImageUpload(@RequestParam(name = "images") MultipartFile[] images,
                                                          @RequestParam(name = "path-base", required = false)String pathBase) throws FileUploadException {
        if (StringUtils.isBlank(pathBase)) {
            pathBase = getPathBase();
        }

        List<ImmutableMap> list = new LinkedList<>();

        for (MultipartFile img : images) {
            if (StringUtils.equals(img.getContentType(), MediaType.IMAGE_GIF_VALUE) || StringUtils.equals(img.getContentType(), MediaType.IMAGE_JPEG_VALUE) ||
                    StringUtils.equals(img.getContentType(), MediaType.IMAGE_PNG_VALUE)) {
                StringBuilder imageName = new StringBuilder();

                imageName.append(System.currentTimeMillis()).append(".").append(Objects.requireNonNull(img.getContentType()).split("/")[1]);

                try {
                    File file = new File(IMAGE_ROOT_DIR_PATH + pathBase + imageName);

                    img.transferTo(Paths.get(file.getPath()));

                    list.add(ImmutableMap.<String, Object>builder()
                            .put("pathBase", pathBase)
                            .put("imageName", imageName.toString())
                            .put("imageOriginName", img.getOriginalFilename())
                            .put("imageURL", "/images/" + pathBase + imageName)
                            .build());
                } catch (IOException e) {
                    throw new FileUploadException("image file upload error !!");
                }
            }
        }

        return success(list);
    }

    @Secured(RoleType.USER)
    @DeleteMapping
    public ApiResult<Boolean> actionImageDelete(@RequestParam(name = "path-base")String pathBase,
                                                @RequestParam(name = "image-name")String imageName) throws FileNotFoundException {
        File deleteFile = new File(IMAGE_ROOT_DIR_PATH + pathBase + imageName);

        if (! deleteFile.delete()) {
            throw new FileNotFoundException("image file delete error !!");
        }

        return success(null);
    }

    private String getPathBase() {
        Calendar calendar = Calendar.getInstance();
        StringBuilder builder = new StringBuilder();

        builder.append(calendar.get(Calendar.YEAR)).append("/").append(calendar.get(Calendar.MONTH) + 1).append("/")
                .append(calendar.get(Calendar.DATE)).append("/").append(calendar.getTimeInMillis()).append("/");

        File file = new File(IMAGE_ROOT_DIR_PATH + builder);

        if (file.mkdirs()) {
            logger.debug("path base directory create done!!");
        } else{
            logger.error("path base directory create error!!");
        }

        return builder.toString();
    }
}

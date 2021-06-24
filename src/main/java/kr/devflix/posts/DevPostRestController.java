package kr.devflix.posts;

import kr.devflix.errors.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static kr.devflix.utils.ApiUtils.ApiResult;
import static kr.devflix.utils.ApiUtils.success;

@RestController
@RequestMapping("/a/dev-posts")
public class DevPostRestController {

    private final DevPostService devPostService;

    public DevPostRestController(DevPostService devPostService) {
        this.devPostService = devPostService;
    }

    @GetMapping
    public ApiResult<Page<DevPostDto>> devPostList(@RequestParam(required = false) String category,
                                      @RequestParam(required = false) String tag,
                                      @RequestParam(required = false) String search,
                                      @RequestParam Integer page,
                                      @RequestParam Integer size) {
        Page<DevPostDto> findAll = devPostService.findAllByCategoryOrTagOrSearchOrPage(category, tag, search, page, size)
                .map(DevPostDto::new);

        if (findAll.isEmpty()) {
            throw new NotFoundException("could not found dev post");
        }

        return success(findAll);
    }
}

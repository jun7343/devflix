package kr.devflix.posts;

import kr.devflix.errors.NotFoundException;
import kr.devflix.utils.Pagination;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ApiResult<List<DevPostDto>> actionDevPostList(@RequestParam(required = false)String c,
                                      @RequestParam(required = false)String t,
                                      @RequestParam(required = false)String s,
                                      @RequestParam(required = false, defaultValue = "0")int page,
                                      @RequestParam(required = false, defaultValue = "20", name = "per-page")int perPage) {
        List<DevPostDto> findAll = devPostService.findAllByCategoryOrTagOrSearch(c, t, s, page, perPage);

        if (findAll.isEmpty()) {
            throw new NotFoundException("could not found posts");
        }

        return success(findAll);
    }

    @GetMapping("/page")
    public ApiResult<Pagination> actionDevPostPagination(@RequestParam(required = false, defaultValue = "0")int page,
                                                   @RequestParam(required = false, defaultValue = "20", name = "per-page")int perPage,
                                                   @RequestParam(required = false, defaultValue = "5", name = "page-list-size")int pageListSize,
                                                   @RequestParam(required = false)String c,
                                                   @RequestParam(required = false)String t,
                                                   @RequestParam(required = false)String s) {
        Long totalCount = devPostService.countByCategoryOrTagOrSearch(c, t, s);

        if (totalCount == null) {
            throw new NotFoundException("could not found posts");
        }

        return success(new Pagination(page, perPage, totalCount, pageListSize));
    }

    @PatchMapping("/view/{id}")
    public ApiResult<Long> actionDevPostViewCountUpdate(@PathVariable Long id,
                                               @RequestParam(name = "id")Long postId) {
        return success(devPostService.updateViewById(id));
    }
}

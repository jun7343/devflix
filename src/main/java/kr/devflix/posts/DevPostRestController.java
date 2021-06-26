package kr.devflix.posts;

import kr.devflix.errors.NotFoundException;
import kr.devflix.utils.Pagination;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
    public ApiResult<List<DevPostDto>> devPostList(@RequestParam(required = false)String c,
                                      @RequestParam(required = false)String t,
                                      @RequestParam(required = false)String s,
                                      @RequestParam(required = false, defaultValue = "0")Integer page,
                                      @RequestParam(required = false, defaultValue = "20")Integer resultMax) {
        List<DevPostDto> findAll = devPostService.findAllByCategoryOrTagOrSearch(c, t, s, page, resultMax)
                .stream()
                .map(DevPostDto::new)
                .collect(Collectors.toList());

        if (findAll.isEmpty()) {
            throw new NotFoundException("could not found posts");
        }

        return success(findAll);
    }

    @GetMapping("/page")
    public ApiResult<Pagination> devPostPagination(@RequestParam(required = false, defaultValue = "0")Integer page,
                                                   @RequestParam(required = false, defaultValue = "20")Integer resultMax,
                                                   @RequestParam(required = false, defaultValue = "5")Integer pageListSize,
                                                   @RequestParam(required = false)String c,
                                                   @RequestParam(required = false)String t,
                                                   @RequestParam(required = false)String s) {
        Long totalCount = devPostService.countByCategoryOrTagOrSearch(c, t, s);

        if (totalCount == null) {
            throw new NotFoundException("could not found posts");
        }

        Pagination pagination = new Pagination(page, resultMax, totalCount, pageListSize);

        return success(pagination);
    }
}

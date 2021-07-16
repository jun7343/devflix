package kr.devflix.controller;

import kr.devflix.dto.DevPostDto;
import kr.devflix.errors.NotFoundException;
import kr.devflix.service.DevPostService;
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
    public ApiResult<List<DevPostDto>> getDevPostList(@RequestParam(required = false)String c,
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

    @GetMapping("/pagination")
    public ApiResult<Pagination> getDevPostPagination(@RequestParam(required = false, defaultValue = "0")int page,
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

    @PatchMapping("/{id}")
    public ApiResult<Long> actionDevPostViewCountUpdate(@PathVariable Long id) throws IllegalAccessException {
        Long result = devPostService.updateViewCountById(id);

        if (result != 1) {
            throw new IllegalAccessException("devPost update error !");
        }

        return success(result);
    }
}

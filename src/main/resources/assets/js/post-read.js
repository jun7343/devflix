$(function () {
    const $COMMENT = $('#comment');
    const $COMMENT_LIST = $('#comment-list');
    const API_COMMENT_SAVE_URL = '/a/post-comment-save';
    const API_COMMENT_DRAWING_URL = '/a/post-comment-list';
    const API_VIEW_COUNT_URL = '/a/view-count';
    const API_COMMENT_DELETE_URL = '/a/post-comment-delete';
    const POST_TYPE = 'POST';
    const POST_ID = $('#content').data('id');
    const MATCH_IMG_URL = '{{img-url}}';
    const MATCH_WRITER = '{{writer}}';
    const MATCH_COMMENT = '{{comment}}';
    const MATCH_UPLOAD_AT = '{{uploadAt}}';
    const MATCH_VALUE = '{{value}}';
    const MATCH_NUM = '{{num}}';
    const MATCH_ID = '{{id}}';
    const DEFAULT_PAGE = 9999;
    const COMMENT_TEMPLATE = '<div class="comment-item col-md-12" data-comment="{{id}}"><div class="user-profile"><img class="img-rounded" src="{{img-url}}">{{writer}}</div><div class="comment-content"><small>{{uploadAt}}</small><br>{{comment}}</div></div>';
    const COMMENT_DROP_DOWN_TEMPLATE = '<div class="btn-group comment-menu"><button type="button" class="btn btn-secondary dropdown-toggle dropdown-toggle-split btn-sm" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="sr-only">Toggle Dropdown</span></button>' +
        '<div class="dropdown-menu"><a class="dropdown-item comment-modify">수정</a><a class="dropdown-item comment-delete">삭제</a></div></div>';
    const PAGING_TEMPLATE = '<nav id="nav-pagination" aria-label="Page navigation"><ul class="pagination justify-content-center"></ul></nav>';
    const PAGING_PREVIOUS_TEMPLATE = '<li class="page-item"><button class="page-link" aria-label="Previous" value="{{value}}"><span aria-hidden="true">&laquo;</span><span class="sr-only">Previous</span></button></li>';
    const PAGING_NEXT_TEMPLATE = '<li class="page-item"><button class="page-link" aria-label="Next" value="{{value}}"><span aria-hidden="true">&raquo;</span><span class="sr-only">Next</span></button></li>';
    const PAGING_NUM_TEMPLATE = '<li class="page-item"><button class="page-link" value="{{value}}">{{num}}</button></li>';

    $COMMENT.summernote({
        toolbar: false,
        height: 120
    });

    commentDrawing(DEFAULT_PAGE);

    $(document).on('click', '.page-link', function () {
        commentDrawing($(this).val());
    });

    $('#delete').on('click', function () {
        return confirm('정말 삭제 하시겠습니까??');
    });

    $('.view-anchor').on('click', function () {
        $.ajax({
            url: API_VIEW_COUNT_URL,
            type: POST_TYPE,
            data: {'url': $(this).attr('href')},
            success: function (data) {
            }
        })
    });

    $(document).on('click', '.comment-delete', function (e) {
        e.preventDefault();
        const ID = $(this).parent().parent().parent().data('comment');

        if (confirm('삭제 하시겠습니까?')) {
            $.ajax({
                url: API_COMMENT_DELETE_URL,
                type: POST_TYPE,
                data: {'id': ID},
                success:function () {
                    commentDrawing(DEFAULT_PAGE);
                }
            });
        }
    });

    $(document).on('click', '.comment-modify', function (e) {
        location.href = '/post/comment?pId=' + POST_ID + '&cId=' +  $(this).parent().parent().parent().data('comment');
    });

    $('#comment-form').on('submit', function (e) {
        e.preventDefault();

        $.ajax({
            url: API_COMMENT_SAVE_URL,
            type: POST_TYPE,
            data: $(this).serialize(),
            success: function () {
                $COMMENT.summernote('reset');
                commentDrawing(DEFAULT_PAGE);
            }
        })
    });

    function commentDrawing(page) {
        $COMMENT_LIST.empty();
        $COMMENT_LIST.append('<h3>Comments</h3>');

        $.ajax({
            url: API_COMMENT_DRAWING_URL,
            type: POST_TYPE,
            data: {'post-id': POST_ID, 'page': page},
            success: function (data) {
                if (data.commentList) {
                    for (const comment of data.commentList) {
                        const TEMPLATE = COMMENT_TEMPLATE.replaceAll(MATCH_IMG_URL, comment.userImg)
                            .replaceAll(MATCH_ID, comment.id)
                            .replaceAll(MATCH_WRITER, comment.writer)
                            .replaceAll(MATCH_COMMENT, comment.comment)
                            .replaceAll(MATCH_UPLOAD_AT, comment.uploadAt);

                        $COMMENT_LIST.append($(TEMPLATE));

                        if (comment.owner) {
                            $COMMENT_LIST.find('.comment-item').last().find('.user-profile').eq(0).addClass('active-comment');
                            $COMMENT_LIST.find('.comment-item').last().append($(COMMENT_DROP_DOWN_TEMPLATE));
                        }
                    }
                }

                if (data.paging) {
                    let $resultPaging = $(PAGING_TEMPLATE);

                    if (data.paging.previousPage) {
                        const PREVIOUS_TEMPATE = PAGING_PREVIOUS_TEMPLATE.replaceAll(MATCH_VALUE, data.paging.previousPageNum);

                        $resultPaging.find('ul').eq(0).append($(PREVIOUS_TEMPATE));
                    }

                    for (const idx in data.paging.pageNumList) {
                        const NUM_TEMPLATE = PAGING_NUM_TEMPLATE.replaceAll(MATCH_VALUE, data.paging.pageNumList[idx] - 1)
                            .replaceAll(MATCH_NUM, data.paging.pageNumList[idx]);

                        $resultPaging.find('ul').eq(0).append($(NUM_TEMPLATE));

                        if (data.paging.pageNumList[idx] === data.paging.currentPageNum) {
                            $resultPaging.find('li').last().addClass('active');
                        }
                    }

                    if (data.paging.nextPage) {
                        const NEXT_TEMPLATE = PAGING_NEXT_TEMPLATE.replaceAll(MATCH_VALUE, data.paging.nextPageNum);

                        $resultPaging.find('ul').eq(0).append($(NEXT_TEMPLATE));
                    }

                    $COMMENT_LIST.append($resultPaging);
                }
            }
        })
    }
})
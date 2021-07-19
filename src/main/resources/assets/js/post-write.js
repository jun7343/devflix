$(function () {
    const API_DEV_POST_URL = '/a/dev-posts';
    const API_IMAGE_URL = '/a/images';
    const MATCH_URL = '{{url}}';
    const MATCH_THUMBNAIL = '{{thumbnail}}';
    const MATCH_TITLE = '{{title}}';
    const MATCH_UPLOAD_AT = '{{uploadAt}}';
    const MATCH_CATEGORY = '{{category}}';
    const MATCH_POST_TYPE = '{{postType}}';
    const $SEARCH_LIST_DROPDOWN = $('#search-dropdown');
    const $SEARCH_LIST = $('#search-list');
    const $SEARCH_RESULT_LIST = $('#result-list');
    const $EDITOR_CONTAINER = $('#content');
    const $PATH_BASE = $('#path-base');
    const MATCH_FOR_PERFECT_SEARCH = /[ㄱ-ㅎ|ㅏ-ㅣ]/;
    const SEARCH_LIST_TEMPLATE = '<li class="list-group-item"><a href="#" class="item-link" data-url="{{url}}" data-thumbnail="{{thumbnail}}" data-upload-at="{{uploadAt}}" data-category="{{category}}" data-post-type="{{postType}}" data-title="{{title}}"><span class="item-category">{{category}}-{{postType}}</span><span class="item-title">{{title}}</span></a></li>';
    const SEARCH_RESULT_ITEM_TEMPLATE = '<div class="card col-md-12 bg-light result-item"><div class="row g-0"><div class="col-md-4 result-item-img-div"><img src="{{thumbnail}}" class="result-item-img"></div><div class="col-md-8"><div class="card-body"><button type="button" class="result-item-close btn btn-danger btn-sm">X</button><h5 class="card-title">{{category}} - {{postType}}</h5><p class="card-text">{{title}}</p><p class="card-text"><small class="text-muted">{{uploadAt}}</small></p><input type="hidden" name="post-url" value="{{url}}"></div></div></div></div>';
    const CSRF_TOKEN = $('meta[name="_csrf"]').attr('content');
    const CSRF_HEADER = $('meta[name="_csrf_header"]').attr('content');
    let search = null;
    let lastSearchContent = '';

    $EDITOR_CONTAINER.summernote({
        lang: 'ko-KR',
        height: 500,
        toolbar: [
            ['style', ['style']],
            ['font', ['bold', 'underline', 'clear']],
            ['color', ['color']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['table', ['table']],
            ['insert', ['link', 'picture', 'video']],
            ['view', ['fullscreen', 'help']]
        ],
        codeviewFilter: true,
        codeviewIframeFilter: true,
        callbacks: {
            onImageUpload: function (files, editor, welEditable) {
                uploadImage(files, $PATH_BASE.val());
            },
            onMediaDelete: function (files, editor, welEditable) {
                deleteImage($PATH_BASE.val(), files[0].id);
            }
        }
    });

    function uploadImage(files, pathBase) {
        const data = new FormData();

        for (const file of files) {
            data.append('images', file);
        }

        data.append('path-base', pathBase);

        $.ajax({
            url: API_IMAGE_URL,
            type: 'POST',
            beforeSend: function (xhr) {
                xhr.setRequestHeader(CSRF_HEADER, CSRF_TOKEN);
            },
            data: data,
            contentType: false,
            cache: false,
            processData: false,
            success: function (data) {
              if ($PATH_BASE.val() === '' || $PATH_BASE.val() === undefined) {
                  $PATH_BASE.val(data.response[0].pathBase);
              }

              for (const item of data.response) {
                  const input = document.createElement('input');
                  input.type = 'hidden';
                  input.name = 'images';
                  input.value = item.imageName;
                  $EDITOR_CONTAINER.append(input);

                  $EDITOR_CONTAINER.summernote('insertNode', $('<img>').attr('src', item.imageURL).attr('id', item.imageName)[0]);
              }
            }
        })
    }

    function deleteImage(pathBase, imageName) {
        console.log('path base = ' + pathBase + ' image name = ' + imageName);
        $.ajax({
            url: API_IMAGE_URL + '?path-base=' + pathBase + '&image-name=' + imageName,
            type: 'DELETE',
            beforeSend: function (xhr) {
                xhr.setRequestHeader(CSRF_HEADER, CSRF_TOKEN);
            },
            success: function (data) {
            }
        })
    }

    $('#postForm').validate({
        rules: {
            'title': {
                required: true,
                maxlength: 2000
            }
        },
        messages: {
            'title': {
                required: '제목 기입해 주세요.',
                maxlength: '최대 2000자 까지 입력해 주세요.'
            }
        }
    })

    $(document).on('click', '.result-item-close', function () {
        $(this).parent().parent().parent().parent().remove();

        if ($SEARCH_RESULT_LIST.children().length <= 1) {
            $SEARCH_RESULT_LIST.css('display', 'none');
        }

        if ($('.alert-danger').css('display') === 'block') {
            $('.alert-danger').css('display', 'none');
        }
    });

    $(document).on('click', '.item-link', function (e) {
        e.preventDefault();
        $SEARCH_LIST_DROPDOWN.css('display', 'none');
        $SEARCH_RESULT_LIST.css('display', 'block');

        if ($SEARCH_RESULT_LIST.children().length < 2) {
            const TEMPLATE = SEARCH_RESULT_ITEM_TEMPLATE.replaceAll(MATCH_URL, $(this).data('url'))
                .replaceAll(MATCH_TITLE, $(this).data('title'))
                .replaceAll(MATCH_UPLOAD_AT, $(this).data('uploadAt'))
                .replaceAll(MATCH_THUMBNAIL, $(this).data('thumbnail'))
                .replaceAll(MATCH_CATEGORY, $(this).data('category'))
                .replaceAll(MATCH_POST_TYPE, $(this).data('postType'));

            $SEARCH_RESULT_LIST.append($(TEMPLATE));
        } else {
            $('.alert-danger').css('display', 'block');
        }
    });

    $('#dev-search').on('click', function () {
        $SEARCH_LIST_DROPDOWN.css('display', 'block');
    });

    $('#dev-search').on('keyup', function () {
        if (search != null) {
            clearTimeout(search);
        }

        search = setTimeout(function () {
            const CONTENT = $('#dev-search').val();

            if (CONTENT.length > 0) {
                $SEARCH_LIST_DROPDOWN.css('display', 'block');

                if (!MATCH_FOR_PERFECT_SEARCH.test(CONTENT) && CONTENT.charAt(CONTENT.length - 1) !== ' ' && CONTENT.length > 0 && CONTENT !== lastSearchContent) {
                    $.ajax({
                        url: API_DEV_POST_URL,
                        type: 'GET',
                        data: { 's': CONTENT },
                        success: function (data) {
                            $SEARCH_LIST.empty();
                            lastSearchContent = CONTENT;

                            for (const item of data.response) {
                              const ITEM_TEMPLATE = SEARCH_LIST_TEMPLATE.replaceAll(MATCH_URL, item.url)
                                                          .replaceAll(MATCH_CATEGORY, item.category)
                                                          .replaceAll(MATCH_POST_TYPE, item.postType)
                                                          .replace(MATCH_TITLE, item.title)
                                                          .replace(MATCH_TITLE, item.title.replaceAll(CONTENT, '<mark>' + CONTENT + '</mark>'))
                                                          .replaceAll(MATCH_THUMBNAIL, item.thumbnail)
                                                          .replaceAll(MATCH_UPLOAD_AT, item.uploadAt);

                              $SEARCH_LIST.append($(ITEM_TEMPLATE));
                            }
                        },
                        error: function(e) {
                          $SEARCH_LIST.empty();

                          const ITEM_TEMPLATE = SEARCH_LIST_TEMPLATE.replaceAll(MATCH_URL, '')
                                                    .replaceAll(MATCH_CATEGORY, '')
                                                    .replaceAll(MATCH_POST_TYPE, '')
                                                    .replaceAll(MATCH_TITLE, 'No search results ...')
                                                    .replaceAll(MATCH_THUMBNAIL, '')
                                                    .replaceAll(MATCH_UPLOAD_AT, '');

                          $SEARCH_LIST.append($(ITEM_TEMPLATE));
                        }
                    });
                }
            } else {
                $SEARCH_LIST_DROPDOWN.css('display', 'none');
            }
        }, 200);
    });
})
$(function () {
    const API_VIEW_COUNT_URL = '/a/view-count';
    const API_DEV_POST_DRAWING_URL = '/a/dev-post-list';
    const POST_TYPE = 'POST';
    const $DEV_POST_LIST = $('#grid');
    const DEV_POST_LIST_TYPE = $DEV_POST_LIST.data('type');
    const DEV_POST_LIST_PARAMETER = $DEV_POST_LIST.data('parameter');
    const MATCH_CATEGORY = '{{category}}';
    const MATCH_POST_TYPE = '{{postType}}';
    const MATCH_TITLE = '{{title}}';
    const MATCH_DESCRIPTION = '{{description}}';
    const MATCH_UPLOAD_AT = '{{uploadAt}}';
    const MATCH_THUMBNAIL = '{{thumbnail}}';
    const MATCH_URL = '{{url}}';
    const MATCH_IS_NEW = '{{isNew}}';
    const MATCH_TAG = '{{tag}}'
    const MATCH_VALUE = '{{value}}';
    const MATCH_NUM = '{{num}}';
    const MATCH_ICON = '{{icon}}';
    const DEV_POST_WATCH_ICON_TEMPLATE = '<div class="watch-icon"><svg><use xlink:href="#icon-watch"></use></svg></div>';
    const DEV_POST_READ_ICON_TEMPLATE = '<div class="read-icon"><svg><use xlink:href="#icon-read"></use></svg></div>';
    const DEV_POST_TEMPLATE = '<article class="box-item"><span class="category"><a href="/category?c={{category}}"><span>{{category}} - {{postType}}</span></a></span><div class="box-body">' +
            '<a class="cover view-anchor" href="{{url}}" target="_blank"><svg width="50" height="50" viewBox="0 0 38 38" xmlns="http://www.w3.org/2000/svg" class="loader"><defs>' +
        '<linearGradient x1="0%" y1="100%" x2="100%" y2="100%" id="a"><stop stop-color="currentColor" stop-opacity="0" offset="0%"/><stop stop-color="currentColor" stop-opacity=".631" offset="63.146%"/><stop stop-color="currentColor" offset="100%"/>' +
        '</linearGradient></defs><g fill="none" fill-rule="evenodd"><g transform="translate(1 1)"><path d="M0,18.0000525 C0,27.9411416 8.05885836,36 18.0000525,36 C27.9411416,36 36,27.9411416 36,18.0000525" id="Oval-2" stroke="url(#a)" stroke-width="2">' +
        '<animateTransform tattributeName="transform" type="rotate" from="360 18 18" to="0 18 18" dur="1.9s" repeatCount="indefinite"/></path><circle fill="currentColor" cx="36" cy="18" r="1">' +
        '<animateTransform tattributeName="transform" type="rotate" from="360 18 18" to="0 18 18" dur="1.9s" repeatCount="indefinite"/></circle>\</g></g></svg>{{icon}}' +
        '<img src="/assets/img/placeholder.png" width="100%" data-url="{{thumbnail}}" class="preload"><noscript><img src="{{thumbnail}}" width="100%"></noscript>{{isNew}}' +
        '</a><div class="box-info"><time datetime="{{uploadAt}}" class="date">{{uploadAt}}</time><a class="post-link view-anchor" href="{{url}}" target="_blank"><h2 class="post-title">{{title}}</h2></a>\<a class="post-link view-anchor" href="{{url}}" target="_blank">' +
        '<p class="description">{{description}}</p></a>{{tag}}</div></div></article>';
    const $PAGINATION = $('#pagination');
    const PAGING_PREVIOUS_TEMPLATE = '<a class="page-item previous" data-val="{{value}}"><svg><use xlink:href="#icon-arrow-right"></use></svg></a>';
    const PAGING_NEXT_TEMPLATE = '<a class="page-item next" data-val="{{value}}"><svg><use xlink:href="#icon-arrow-right"></use></svg></a>';
    const PAGING_NUM_TEMPLATE = '<a class="page-item page-number" data-val="{{value}}"><span>{{num}}</span></a>';

    devPostDrawling(DEV_POST_LIST_TYPE, DEV_POST_LIST_PARAMETER, getParameterByName('page'));

    $('.view-anchor').on('click', function () {
        $.ajax({
            url: API_VIEW_COUNT_URL,
            type: POST_TYPE,
            data: {'url': $(this).attr('href')},
            success: function (data) {
            }
        })
    });

    $(document).on('click', '.page-item', function (e) {
        e.preventDefault();
        const VALUE = $(this).data('val');

        history.pushState({'page' : VALUE}, 'main',  '?page=' + VALUE);

        $('html, body').stop().animate({
            scrollTop: $DEV_POST_LIST.offset().top - $('.bar-header').eq(0).innerHeight()
        }, 800);

        devPostDrawling(DEV_POST_LIST_TYPE, DEV_POST_LIST_PARAMETER, VALUE);
    });

    $(window).on('popstate', function () {
        devPostDrawling(DEV_POST_LIST_TYPE, DEV_POST_LIST_PARAMETER, getParameterByName('page'));
    });

    function getParameterByName (name) {
        name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
        const regex = new RegExp('[\\?&]' + name + '=([^&#]*)'), results = regex.exec(location.search);
        return results == null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    }

    function devPostDrawling(type, parameter, page) {
        $.ajax({
            url: API_DEV_POST_DRAWING_URL,
            type: POST_TYPE,
            data: {'type': type, 'parameter': parameter, 'page': page},
            success: function (data) {
                $DEV_POST_LIST.empty();

                for (const post of data.devPostList) {
                    let template = DEV_POST_TEMPLATE.replaceAll(MATCH_TITLE, post.title)
                        .replaceAll(MATCH_UPLOAD_AT, post.uploadAt)
                        .replaceAll(MATCH_CATEGORY, post.category)
                        .replaceAll(MATCH_THUMBNAIL, post.thumbnail)
                        .replaceAll(MATCH_URL, post.url)
                        .replaceAll(MATCH_POST_TYPE, post.postType)
                        .replaceAll(MATCH_ICON, post.postType === 'BLOG'? DEV_POST_READ_ICON_TEMPLATE : DEV_POST_WATCH_ICON_TEMPLATE)
                        .replaceAll(MATCH_IS_NEW, post.isNew? '<div class="new-post-tag">New Post</div>' : '')
                        .replaceAll(MATCH_DESCRIPTION, post.description.length > 100? post.description.substring(0, 100) + "..." : post.description);

                    let tagTemplate = '';

                    if (post.tagList) {
                        tagTemplate = '<div class="tags">';

                        for (const tag of post.tagList) {
                            tagTemplate += '<a href="/tag?t=' + tag + '">#' + tag + '</a>';
                        }
                        tagTemplate += '</div>';
                    }

                    template = template.replaceAll(MATCH_TAG, tagTemplate);

                    $DEV_POST_LIST.append($(template));
                }

                new AnimOnScroll( document.getElementById( 'grid' ), {
                    minDuration : 0.4,
                    maxDuration : 0.7,
                    viewportFactor : 0.2
                });

                if (data.paging) {
                    $PAGINATION.empty();

                    if (data.paging.previousPage) {
                        const PREVIOUS_TEMPATE = PAGING_PREVIOUS_TEMPLATE.replaceAll(MATCH_VALUE, data.paging.previousPageNum);

                        $PAGINATION.append($(PREVIOUS_TEMPATE));
                    }

                    for (const idx in data.paging.pageNumList) {
                        const NUM_TEMPLATE = PAGING_NUM_TEMPLATE.replaceAll(MATCH_VALUE, data.paging.pageNumList[idx] - 1)
                            .replaceAll(MATCH_NUM, data.paging.pageNumList[idx]);

                        $PAGINATION.append($(NUM_TEMPLATE));

                        if (data.paging.pageNumList[idx] === data.paging.currentPageNum) {
                            $PAGINATION.find('a').last().addClass('page-active');
                        }
                    }

                    if (data.paging.nextPage) {
                        const NEXT_TEMPLATE = PAGING_NEXT_TEMPLATE.replaceAll(MATCH_VALUE, data.paging.nextPageNum);

                        $PAGINATION.append($(NEXT_TEMPLATE));
                    }
                }
            }
        })
    }
})
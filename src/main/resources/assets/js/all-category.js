$(function () {
    const API_ALL_CATEGORY_URL = '/a/all-category';
    const POST_TYPE = 'POST';
    const $CATEGORY_LIST = $('#grid');
    const MATCH_CATEGORY = '{{category}}';
    const MATCH_THUMBNAIL = '{{thumbnail}}';
    const CATEGORY_TEMPLATE = '<article class="box-item"><div class="box-body">' +
        '<a class="cover view-anchor" href="/category?c={{category}}"><svg width="50" height="50" viewBox="0 0 38 38" xmlns="http://www.w3.org/2000/svg" class="loader"><defs>' +
        '<linearGradient x1="0%" y1="100%" x2="100%" y2="100%" id="a"><stop stop-color="currentColor" stop-opacity="0" offset="0%"/><stop stop-color="currentColor" stop-opacity=".631" offset="63.146%"/><stop stop-color="currentColor" offset="100%"/>' +
        '</linearGradient></defs><g fill="none" fill-rule="evenodd"><g transform="translate(1 1)"><path d="M0,18.0000525 C0,27.9411416 8.05885836,36 18.0000525,36 C27.9411416,36 36,27.9411416 36,18.0000525" id="Oval-2" stroke="url(#a)" stroke-width="2">' +
        '<animateTransform tattributeName="transform" type="rotate" from="360 18 18" to="0 18 18" dur="1.9s" repeatCount="indefinite"/></path><circle fill="currentColor" cx="36" cy="18" r="1">' +
        '<animateTransform tattributeName="transform" type="rotate" from="360 18 18" to="0 18 18" dur="1.9s" repeatCount="indefinite"/></circle>\</g></g></svg>' +
        '<img src="/assets/img/placeholder.png" width="100%" data-url="{{thumbnail}}" class="preload"><noscript><img src="{{thumbnail}}" width="100%"></noscript>' +
        '</a><div class="box-info"><a class="post-link" href="/category?c={{category}}"><h2 class="post-title">{{category}}</h2></a></div></div></article>';
    const CSRF_TOKEN = $('meta[name="_csrf"]').attr('content');
    const CSRF_HEADER = $('meta[name="_csrf_header"]').attr('content');

    categoryListDrawing('blog');

    $('#blog').on('click', function (e) {
        e.preventDefault();
        $('#youtube').removeClass('active');
        $('#blog').addClass('active');

        categoryListDrawing('blog');
    });

    $('#youtube').on('click', function (e) {
        e.preventDefault();
        $('#blog').removeClass('active');
        $('#youtube').addClass('active');

        categoryListDrawing('youtube');
    });

    function categoryListDrawing(type) {
        $.ajax({
            url: API_ALL_CATEGORY_URL,
            type: POST_TYPE,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(CSRF_HEADER, CSRF_TOKEN);
            },
            data: {'type': type},
            success: function (data) {
                if (data.result) {
                    $CATEGORY_LIST.empty();

                    for (const item of data.result) {
                        const TEMPLATE = CATEGORY_TEMPLATE.replaceAll(MATCH_CATEGORY, item.category)
                            .replaceAll(MATCH_THUMBNAIL, item.thumbnail);

                        $CATEGORY_LIST.append($(TEMPLATE));
                    }
                }

                new AnimOnScroll( document.getElementById( 'grid' ), {
                    minDuration : 0.4,
                    maxDuration : 0.7,
                    viewportFactor : 0.2
                });
            }
        });
    }
})
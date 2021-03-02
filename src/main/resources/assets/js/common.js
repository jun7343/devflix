$(function () {
    const API_VIEW_COUNT_URL = '/a/view-count';
    const API_SEARCH_URL = '/a/search';
    const POST_TYPE = 'POST';
    const REGEX_URL = '{{url}}';
    const REGEX_THUMBNAIL = '{{thumbnail}}';
    const REGEX_TITLE = '{{title}}';
    const REGEX_UPLOAD_AT = '{{uploadAt}}';
    const REGEX_CATEGORY = '{{category}}';
    const REGEX_POST_TYPE = '{{postType}}';
    const $SEARCH_RESULT_FIELD = $('#search-ul');
    const REGEX_PERFECT_SEARCH = /[ㄱ-ㅎ|ㅏ-ㅣ]/;
    const SEARCH_TEMPLATE = '<li><div class="search-thumbnail"><a href="{{url}}"><img src="{{thumbnail}}"></a></div>' +
        '<div class="search-content"><a href="{{url}}"><span class="category">{{category}} - {{postType}}</span><br><p>{{title}}</p></a></div><div class="search-date">' +
        '<a href="{{url}}"><span class="entry-date"><time datetime="{{uploadAt}}"></time>{{uploadAt}}</time></span></a></div></li>';

    $('.view-anchor').on('click', function () {
        $.ajax({
            url: API_VIEW_COUNT_URL,
            type: POST_TYPE,
            data: {'url': $(this).attr('href')},
            success: function (data) {
            }
        })
    });

    $('#search-input').on('keyup', function () {
        const CONTENT = $(this).val();
        const LAST_CHAR = CONTENT.charAt(CONTENT.length - 1);

        if (! REGEX_PERFECT_SEARCH.test(CONTENT) && LAST_CHAR !== ' ') {
            $SEARCH_RESULT_FIELD.empty();

            $.ajax({
                url: API_SEARCH_URL,
                type: POST_TYPE,
                data: {'content': $(this).val()},
                success: function (data) {
                    if (data.result) {
                        for (const item of data.data) {
                            const ITEM_TEMPLATE = SEARCH_TEMPLATE.replaceAll(REGEX_URL, item.url)
                                .replaceAll(REGEX_CATEGORY, item.category)
                                .replaceAll(REGEX_POST_TYPE, item.postType)
                                .replaceAll(REGEX_TITLE, item.title.replace(CONTENT, '<mark>' + CONTENT + '</mark>'))
                                .replaceAll(REGEX_THUMBNAIL, item.thumbnail)
                                .replaceAll(REGEX_UPLOAD_AT, item.uploadAt);

                            $SEARCH_RESULT_FIELD.append($(ITEM_TEMPLATE));
                        }
                    }
                }
            });
        }


    });
})
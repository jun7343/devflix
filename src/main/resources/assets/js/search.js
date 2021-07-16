$(function () {
    const API_DEV_POST_URL = '/a/dev-posts';
    const MATCH_URL = '{{url}}';
    const MATCH_THUMBNAIL = '{{thumbnail}}';
    const MATCH_TITLE = '{{title}}';
    const MATCH_UPLOAD_AT = '{{uploadAt}}';
    const MATCH_CATEGORY = '{{category}}';
    const MATCH_POST_TYPE = '{{postType}}';
    const $SEARCH_RESULT_FIELD = $('#search-ul');
    const MATCH_FOR_PERFECT_SEARCH = /[ㄱ-ㅎ|ㅏ-ㅣ]/;
    const SEARCH_TEMPLATE = '<li><div class="search-thumbnail"><a href="{{url}}" target="_blank"><img src="{{thumbnail}}"></a></div>' +
        '<div class="search-content"><a href="{{url}}" target="_blank"><span class="category">{{category}} - {{postType}}</span><br><p>{{title}}</p></a></div><div class="search-date">' +
        '<a href="{{url}}" target="_blank"><span class="entry-date"><time datetime="{{uploadAt}}"></time>{{uploadAt}}</time></span></a></div></li>';
    let search = null;
    let lastSearchConent = '';


    $('#search-input').on('keyup', function () {
        if (search != null) {
            clearTimeout(search);
        }

        search = setTimeout(function () {
            const TITLE = $('#search-input').val();

            if (! MATCH_FOR_PERFECT_SEARCH.test(TITLE) && TITLE.charAt(TITLE.length - 1) !== ' ' && TITLE.length > 0 && TITLE !== lastSearchConent) {
                $.ajax({
                    url: API_DEV_POST_URL,
                    type: 'GET',
                    data: {'s': TITLE},
                    success: function (data) {
                        $SEARCH_RESULT_FIELD.empty();
                        lastSearchConent = TITLE;

                        if (data.success && data.response) {
                            for (const item of data.response) {
                                const pattern = new RegExp(TITLE, 'gi');

                                const ITEM_TEMPLATE = SEARCH_TEMPLATE.replaceAll(MATCH_URL, item.url)
                                    .replaceAll(MATCH_CATEGORY, item.category)
                                    .replaceAll(MATCH_POST_TYPE, item.postType)
                                    .replaceAll(MATCH_TITLE, item.title.replace(pattern, '<mark>' + TITLE + '</mark>'))
                                    .replaceAll(MATCH_THUMBNAIL, item.thumbnail)
                                    .replaceAll(MATCH_UPLOAD_AT, item.uploadAt);

                                $SEARCH_RESULT_FIELD.append($(ITEM_TEMPLATE));
                            }
                        }
                    },
                    error: function(e) {
                      $SEARCH_RESULT_FIELD.empty();
                    }
                });
            } else if (TITLE.length === 0) {
                $SEARCH_RESULT_FIELD.empty();
            }
        }, 300);
    });
})
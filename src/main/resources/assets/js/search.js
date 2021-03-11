$(function () {
    const API_SEARCH_URL = '/a/search';
    const POST_TYPE = 'POST';
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
            const CONTENT = $('#search-input').val();

            if (! MATCH_FOR_PERFECT_SEARCH.test(CONTENT) && CONTENT.charAt(CONTENT.length - 1) !== ' ' && CONTENT.length > 0 && CONTENT !== lastSearchConent) {
                $.ajax({
                    url: API_SEARCH_URL,
                    type: POST_TYPE,
                    data: {'content': CONTENT},
                    success: function (data) {
                        $SEARCH_RESULT_FIELD.empty();
                        lastSearchConent = CONTENT;

                        if (data.result) {
                            for (const item of data.data) {
                                const ITEM_TEMPLATE = SEARCH_TEMPLATE.replaceAll(MATCH_URL, item.url)
                                    .replaceAll(MATCH_CATEGORY, item.category)
                                    .replaceAll(MATCH_POST_TYPE, item.postType)
                                    .replaceAll(MATCH_TITLE, item.title.replace(CONTENT, '<mark>' + CONTENT + '</mark>'))
                                    .replaceAll(MATCH_THUMBNAIL, item.thumbnail)
                                    .replaceAll(MATCH_UPLOAD_AT, item.uploadAt);

                                $SEARCH_RESULT_FIELD.append($(ITEM_TEMPLATE));
                            }
                        }
                    }
                });
            } else if (CONTENT.length === 0) {
                $SEARCH_RESULT_FIELD.empty();
            }
        }, 300);
    });
})
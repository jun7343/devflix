$(function () {
  const API_SEARCH_URL = '/a/search';
  const POST_TYPE = 'POST';
  const MATCH_URL = '{{url}}';
  const MATCH_THUMBNAIL = '{{thumbnail}}';
  const MATCH_TITLE = '{{title}}';
  const MATCH_UPLOAD_AT = '{{uploadAt}}';
  const MATCH_CATEGORY = '{{category}}';
  const MATCH_POST_TYPE = '{{postType}}';
  const $SEARCH_LIST_DROPDOWN = $('#search-dropdown');
  const $SEARCH_LIST = $('#search-list');
  const $SEARCH_RESULT_LIST = $('#result-list');
  const MATCH_FOR_PERFECT_SEARCH = /[ㄱ-ㅎ|ㅏ-ㅣ]/;
  const SEARCH_LIST_TEMPLATE = '<li class="list-group-item"><a href="#" class="item-link" data-url="{{url}}" data-thumbnail="{{thumbnail}}" data-upload-at="{{uploadAt}}" data-category="{{category}}" data-post-type="{{postType}}" data-title="{{title}}"><span class="item-category">{{category}}-{{postType}}</span><span class="item-title">{{title}}</span></a></li>';
  const SEARCH_RESULT_ITEM_TEMPLATE = '<div class="card col-md-12 bg-light result-item"><div class="row g-0"><div class="col-md-4 result-item-img-div"><img src="{{thumbnail}}" class="result-item-img"></div><div class="col-md-8"><div class="card-body"><button type="button" class="result-item-close btn btn-danger btn-sm">X</button><h5 class="card-title">{{category}} - {{postType}}</h5><p class="card-text">{{title}}</p><p class="card-text"><small class="text-muted">{{uploadAt}}</small></p></div></div></div></div>';

  $('#content').summernote({
    lang: 'ko-KR',
    height: 500,
    toolbar: [
      ['style', ['style']],
      ['font', ['bold', 'underline', 'clear']],
      ['color', ['color']],
      ['para', ['ul', 'ol', 'paragraph']],
      ['table', ['table']],
      ['insert', ['link', 'picture', 'video']],
      ['view', ['fullscreen', 'codeview', 'help']]
    ]
  });

  $(document).on('click', '.result-item-close', function () {
    $(this).parent().parent().parent().parent().remove();

    if ($SEARCH_RESULT_LIST.children().length <= 1) {
      $SEARCH_RESULT_LIST.css('display', 'none');
    } else {
      if ($('.alert-danger').css('display') === 'block') {
        $('.alert-danger').css('display', 'none');
      }
    }
  });

  $(document).on('click', '.item-link', function (e) {
    e.preventDefault();
    $SEARCH_LIST_DROPDOWN.css('display', 'none');
    $SEARCH_RESULT_LIST.css('display', 'block');

    if ($SEARCH_RESULT_LIST.children().length < 6) {
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
    const CONTENT = $('#dev-search').val();

    if (CONTENT.length > 0) {
      $SEARCH_LIST_DROPDOWN.css('display', 'block');
      if (! MATCH_FOR_PERFECT_SEARCH.test(CONTENT) && CONTENT.charAt(CONTENT.length - 1) !== ' ' && CONTENT.length > 0) {
        $.ajax({
          url: API_SEARCH_URL,
          type: POST_TYPE,
          data: {'content': CONTENT},
          success: function (data) {
            $SEARCH_LIST.empty();
            if (data.result) {
              if (data.data.length > 0) {
                for (const item of data.data) {
                  const ITEM_TEMPLATE = SEARCH_LIST_TEMPLATE.replaceAll(MATCH_URL, item.url)
                      .replaceAll(MATCH_CATEGORY, item.category)
                      .replaceAll(MATCH_POST_TYPE, item.postType)
                      .replaceAll(MATCH_TITLE, item.title.replace(CONTENT, '<mark>' + CONTENT + '</mark>'))
                      .replaceAll(MATCH_THUMBNAIL, item.thumbnail)
                      .replaceAll(MATCH_UPLOAD_AT, item.uploadAt.replaceAll("/", "."));

                  $SEARCH_LIST.append($(ITEM_TEMPLATE));
                }
              } else {
                const ITEM_TEMPLATE = SEARCH_LIST_TEMPLATE.replaceAll(MATCH_URL, '')
                    .replaceAll(MATCH_CATEGORY, '')
                    .replaceAll(MATCH_POST_TYPE, '')
                    .replaceAll(MATCH_TITLE, 'No search results ...')
                    .replaceAll(MATCH_THUMBNAIL, '')
                    .replaceAll(MATCH_UPLOAD_AT, '');

                $SEARCH_LIST.append($(ITEM_TEMPLATE));
              }
            }
          }
        });
      }
    } else {
      $SEARCH_LIST_DROPDOWN.css('display', 'none');
    }
  });
})
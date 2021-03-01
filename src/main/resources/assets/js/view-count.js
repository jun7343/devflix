$(function () {
    $('.view-anchor').on('click', function () {
        const ANCHOR_URL = $(this).attr('href');
        const URL = '/a/view-count';
        const TYPE = 'POST';

        if (URL !== '' && URL !== undefined) {
            $.ajax({
                url: URL,
                type: TYPE,
                data: {'url': ANCHOR_URL},
                success: function (data) {
                }
            })
        }
    });
})
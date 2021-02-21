$(function () {
    $('#login-form').validate({
        rules: {
            'email': {
                required: true
            },
            'password': {
                required: true
            }
        },
        messages: {
            'email': {
                required: '이메일 기입해 주세요.'
            },
            'password': {
                required: '패스워드 기입해 주세요.'
            }
        }
    });
})
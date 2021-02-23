$(function () {
    $('#login-form').validate({
        rules: {
            'email': {
                required: true,
                email: true
            },
            'password': {
                required: true
            }
        },
        messages: {
            'email': {
                required: '이메일 기입해 주세요.',
                email: '이메일 형식이 올바르지 않습니다.'
            },
            'password': {
                required: '패스워드 기입해 주세요.'
            }
        }
    });
})
$(function () {
    $('#new-password-form').validate({
        rules: {
            'password': {
                required: true,
                'password-check': true
            },
            'password-confirm': {
                required: true,
                equalTo: ':password'
            }
        },
        messages: {
            'password': {
                required: '패스워드 기입해 주세요.'
            },
            'password-confirm': {
                required: '패스워드 확인 기입해 주세요.',
                equalTo: '패스워드와 똑같이 기입해 주세요.'
            }
        }
    });

    $.validator.addMethod('password-check',  function(value, element) {
        return this.optional(element) ||  /^.*(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/.test(value);
    }, '비밀번호는 영문자, 숫자, 특수문자 조합해 주세요.');
})
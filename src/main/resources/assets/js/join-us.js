$(function () {
    const CODE_CONFIRM_URL = '/login/join-us/code-authentication';
    const EMAIL_CONFIRM_URL = '/login/join-us/email-authentication';
    const POST_TYPE = 'POST';
    const CSRF_TOKEN = $('meta[name="_csrf"]').attr('content');
    const CSRF_HEADER = $('meta[name="_csrf_header"]').attr('content');

    $('#join-us-form').validate({
        rules: {
            'email': {
                required: true,
                email: true
            },
            'username': {
                required: true
            },
            'code': {
                required: true
            },
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
            'email': {
                required: '이메일을 기입해 주세요.',
                email: '이메일 형식이 올바르지 않습니다.'
            },
            'username': {
                required: '유저 이름 기입해 주세요.'
            },
            'code': {
                required: '인증코드 기입해 주세요.'
            },
            'password': {
                required: '패스워드 기입해 주세요.'
            },
            'password-confirm': {
                required: '패스워드 확인 기입해 주세요.',
                equalTo: '패스워드와 똑같이 기입해 주세요.'
            }
        },
        errorPlacement: function(error, element) {
            if (element.parent().attr('class') === 'input-group') {
                error.appendTo(element.parent().parent());
            } else {
                error.appendTo(element.parent());
            }
        },
        submitHandler: function (){
            alert('가입이 완료 되었습니다.');

            return true;
        }
    });

    $.validator.addMethod('password-check',  function(value, element) {
        return this.optional(element) ||  /^.*(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/.test(value);
    }, '비밀번호는 영문자, 숫자, 특수문자 조합해 주세요.');

    $('#btn-email-authentication').on('click', function () {
        const EMAIL = $('input[name=email]').val();

        if (EMAIL === '' || EMAIL === undefined) {
            alert('이메일을 기입해 주세요.');
            return;
        } else if (! (/^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/.test(EMAIL))) {
            alert('이메일 형식이 올바르지 않습니다.');
            return;
        }

        $.ajax({
            url: EMAIL_CONFIRM_URL,
            type: POST_TYPE,
            data: {'email' : EMAIL},
            beforeSend: function (xhr) {
                xhr.setRequestHeader(CSRF_HEADER, CSRF_TOKEN);
            },
            success: function (data) {
                const ALERT_MESSAGE = data.msg;

                if (data.result) {
                    $('#email-confirm').css('display', 'inline');
                }

                alert(ALERT_MESSAGE);
            }
        })
    });

    $('#btn-authentication-check').on('click', function () {
        const EMAIL = $('input[name=email]').val();
        const CODE = $('input[name=code]').val();

        if (EMAIL === '' || EMAIL === undefined) {
            alert('이메일을 기입해 주세요.');
            return;
        } else if (! (/^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/.test(EMAIL))) {
            alert('이메일 형식이 올바르지 않습니다.');
            return;
        }

        $.ajax({
            url: CODE_CONFIRM_URL,
            type: POST_TYPE,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(CSRF_HEADER, CSRF_TOKEN);
            },
            data: {'email' : EMAIL, 'code' : CODE},
            success: function (data) {
                const ALERT_MESSAGE = data.msg;

                alert(ALERT_MESSAGE);
            }
        })
    });
})
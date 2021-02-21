$(function () {
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
            const EMAIL = $('input[name=email]').val();
            const CODE = $('input[name=code]').val();
            const ALL_CONFIRM_URL = '/login/join-us/all-confirm';
            const TYPE = 'POST';
            const DATA = {'email' : EMAIL, 'code' : CODE};

            $.ajax({
                url: ALL_CONFIRM_URL,
                type: TYPE,
                data: DATA,
                success: function (data) {
                    alert(data.msg);
                    return data.result;
                }
            })

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

        const EMAIL_CONFIRM_URL = '/login/join-us/email-authentication';
        const TYPE = 'POST';
        const DATA = {'email' : EMAIL};

        $.ajax({
            url: EMAIL_CONFIRM_URL,
            type: TYPE,
            data: DATA,
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

        const CODE_CONFIRM_URL = '/login/join-us/code-authentication';
        const TYPE = 'POST';
        const DATA = {'email' : EMAIL, 'code' : CODE};

        $.ajax({
            url: CODE_CONFIRM_URL,
            type: TYPE,
            data: DATA,
            success: function (data) {
                const ALERT_MESSAGE = data.msg;

                alert(ALERT_MESSAGE);
            }
        })
    });
})
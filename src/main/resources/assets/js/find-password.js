$(function () {
    $('#find-password-form').validate({
        rules: {
            'email': {
                required: true,
                email: true
            }
        },
        messages: {
            'email': {
                required: '이메일을 기입해 주세요.',
                email: '이메일 형식이 올바르지 않습니다.'
            }
        },
        submitHandler: function (){
            const EMAIL = $('input[name=email]').val();
            const FIND_PASSWORD_CONFIRM_URL = '/login/find-password-confirm';
            const TYPE = 'POST';
            const DATA = {'email' : EMAIL};

            $.ajax({
                url: FIND_PASSWORD_CONFIRM_URL,
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
})
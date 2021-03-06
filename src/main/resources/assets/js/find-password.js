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
            alert('비밀번호 재설정 메일을 성공적으로 요청 하였습니다.')

            return true;
        }
    });
})
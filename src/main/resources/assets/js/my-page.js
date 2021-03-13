$(function () {
    const API_IMAGE_UPLOAD_URL = '/a/image-upload';
    const POST_TYPE = 'POST';
    const $PATH_BASE = $('#path-base');
    const $IMAGE = $('#image');

    $('#password').on('click', function () {
        location.href = '/my-page/password';
    });

    $('#withdrawal').on('click', function () {
        location.href = '/my-page/withdrawal';
    });

    $('a.profile').on('click', function (e) {
        e.preventDefault();

        $('#img-upload').click();
    });

    $('#img-upload').on('change', function (e) {
        uploadImage(e.currentTarget.files[0], $PATH_BASE.val());
    });

    function uploadImage(files, pathBase) {
        const data = new FormData();

        data.append('images', files);
        data.append('path-base', pathBase);

        $.ajax({
            url: API_IMAGE_UPLOAD_URL,
            type: POST_TYPE,
            data: data,
            contentType: false,
            cache: false,
            processData: false,
            success: function (data) {
                console.log(data);
                if (data.result) {
                    if ($PATH_BASE.val() === '' || $PATH_BASE.val() === undefined) {
                        $PATH_BASE.val(data.result[0].pathBase);
                    }

                    if (data.result) {
                        $IMAGE.val(data.result[0].imageName);
                        $('#profile-img').attr('src', data.result[0].imageURL);
                    }
                }
            }
        })
    }

    $.validator.addMethod('password-check',  function(value, element) {
        return this.optional(element) ||  /^.*(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/.test(value);
    }, '비밀번호는 영문자, 숫자, 특수문자 조합해 주세요.');

    $('#my-page-password-form').validate({
        rules: {
            'current-password': {
                required: true
            },
            'new-password': {
                required: true,
                'password-check': true
            },
            'new-password-confirm': {
                equalsTo: ':new-password'
            }
        },
        messages: {
            'current-password': {
                required: '현재 비밀번호 기입해 주세요.'
            },
            'new-password': {
                required: '새로운 비밀번호 기입해 주세요.'
            },
            'new-password-confirm': {
                equalsTo: '새로운 비밀번호와 똑같이 기입해 주세요.'
            }
        }
    });
})
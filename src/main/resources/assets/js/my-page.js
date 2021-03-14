$(function () {
    const API_IMAGE_UPLOAD_URL = '/a/image-upload';
    const POST_TYPE = 'POST';
    const $PATH_BASE = $('#path-base');
    const $IMAGE = $('#image');
    const CSRF_TOKEN = $('meta[name="_csrf"]').attr('content');
    const CSRF_HEADER = $('meta[name="_csrf_header"]').attr('content');

    $('button#password').on('click', function () {
        location.href = '/my-page/password';
    });

    $('button#withdrawal').on('click', function () {
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
            beforeSend: function (xhr) {
                xhr.setRequestHeader(CSRF_HEADER, CSRF_TOKEN);
            },
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

    $('#my-page-form').validate({
        rules: {
            'username': {
                required: true,
                maxLength: 50
            }
        },
        messages: {
            'username': {
                required: '유저 이름 기입해 주세요.',
                maxLength: '유저 이름 최대 50자 입니다.'
            }
        }
    });

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
                required: true,
                equalTo: '#new-password'
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
                required: '새로운 비밀번호 확인 기입해 주세요.',
                equalTo: '새로운 비밀번호와 똑같이 기입해 주세요.'
            }
        }
    });

    $('#my-page-withdrawl-form').validate({
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
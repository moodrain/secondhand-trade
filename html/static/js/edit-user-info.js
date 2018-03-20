let nickInput = document.querySelector('#nick-input');
let nameInput = document.querySelector('#name-input');
let sexSelect = document.querySelector('#sex-select');
let ageInput = document.querySelector('#age-input');
let provinceSelect = document.querySelector('#province-select');
let citySelect = document.querySelector('#city-select');
let districtSelect = document.querySelector('#district-select');
let mailInput = document.querySelector('#mail-input');
let qqInput = document.querySelector('#qq-input');
let wxInput = document.querySelector('#wx-input');
let introductionInput = document.querySelector('#introduction-input');
let oldPasswordInput = document.querySelector('#old-password-input');
let newPasswordInput = document.querySelector('#new-password-input');
let rePasswordInput = document.querySelector('#re-password-input')
let avatarInput = document.querySelector('#avatar-input');
let infoSubmitBtn = document.querySelector('#info-submit-btn');
let passwordSubmitBtn = document.querySelector('#password-submit-btn');
let avatarSubmitBtn = document.querySelector('#avatar-submit-btn');

$(() => {
    initRightBar();
    initUserInfo();
    initSubmitBtn();
});

function initRightBar()
{
    let infoLi = document.querySelector('#edit-info-li');
    let avatarLi = document.querySelector('#edit-avatar-li');
    let passwordLi = document.querySelector('#edit-password-li');
    let infoForm = document.querySelector('#edit-info-form');
    let avatarForm = document.querySelector('#edit-avatar-form');
    let passwordForm = document.querySelector('#edit-password-form');
    infoLi.style.background = 'lightgray';
    let show = (li, form) => {
        infoLi.style.background = 'white';
        avatarLi.style.background = 'white';
        passwordLi.style.background = 'white';
        infoForm.style.display = 'none';
        avatarForm.style.display = 'none';
        passwordForm.style.display = 'none';
        li.style.background = 'lightgray';
        form.style.display = 'block';
    };
    infoLi.addEventListener('click', () => {
        show(infoLi, infoForm);
    });
    avatarLi.addEventListener('click', () => {
        show(avatarLi, avatarForm);
    });
    passwordLi.addEventListener('click', () => {
        show(passwordLi, passwordForm);
    });
}

function initUserInfo()
{
    muyu_get(api + 'user-info', (rs) => {
        let data = rs.data;
        nickInput.value = data.nick;
        nameInput.value = data.name ? data.name : '';
        sexSelect.value = data.sex ? data.sex : '';
        ageInput.value = data.age ? data.age : '';
        $('#distpicker-div').distpicker({
            province: data.province,
            city: data.city,
            district: data.district,
        });
        provinceSelect.value = data.province;
        citySelect.value = data.city;
        districtSelect.value = data.district;
        mailInput.value = data.mail ? data.mail : '';
        qqInput.value = data.qq ? data.qq : '';
        wxInput.value = data.wx ? data.wx : '';
        introductionInput.value = data.introduction;
    });
}

function initSubmitBtn()
{
    infoSubmitBtn.addEventListener('click', () => {
        muyu_post(api + 'edit-user-info', {
            nick: nickInput.value,
            name: nameInput.value,
            sex: sexSelect.value,
            age: ageInput.value,
            province: provinceSelect.value,
            city: citySelect.value,
            district: districtSelect.value,
            mail: mailInput.value,
            qq: qqInput.value,
            wx: wxInput.value,
            introduction: introductionInput.value,
        }, (rs) => {
            if(rs.code === 200)
                muyu_noty('修改成功', 'success');
            else
                muyu_noty(rs.msg);
        });
    });
    passwordSubmitBtn.addEventListener('click', () => {
        if(!(oldPasswordInput.value && newPasswordInput.value && rePasswordInput.value))
        {
            muyu_noty('请输入密码');
            return;
        }
        else if(newPasswordInput.value !== rePasswordInput.value)
        {
            muyu_noty('两次输入的新密码不一样')
            newPasswordInput.value = '';
            rePasswordInput.value = '';
            return;
        }
        muyu_post(api + 'update-password', {
            'oldPassword': oldPasswordInput.value,
            'newPassword': newPasswordInput.value,
        }, (rs) => {
            if(rs.code === 200)
            {
                muyu_noty('修改成功', 'success');
                newPasswordInput.value = '';
                rePasswordInput.value = '';
                oldPasswordInput.value = '';
            }
            else
                muyu_noty(rs.msg);
        });
    });

    avatarSubmitBtn.addEventListener('click', () => {
        let file = avatarInput.files[0];
        if(file == undefined)
        {
            muyu_noty('请选择图片');
            return;
        }
        muyu_file_base64(file, (base) => {
            muyu_post(api + 'upload-avatar', {file: base}, (rs) => {
                if(rs.code === 200)
                {
                    muyu_noty('上传成功', 'success');
                    setTimeout(() => {window.location.reload()}, 2000);
                }
                else
                    muyu_noty(rs.msg);
            });
        });
    });
}
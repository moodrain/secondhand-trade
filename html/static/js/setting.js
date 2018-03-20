let infoPublicSelect = document.querySelector('#info-public-select');
let orderHistoryPublicSelect = document.querySelector('#order-history-public-select');
let submitBtn = document.querySelector('#setting-submit-btn');
let logoutBtn = document.querySelector('#logout-btn');

$(() => {
    initSetting();
    initSubmitBtn();
});

function initSubmitBtn()
{
    submitBtn.addEventListener('click', () => {
        muyu_post(api + 'update-setting', {
            isInfoPublic: infoPublicSelect.value,
            isHistoryPublic: orderHistoryPublicSelect.value,
        }, (rs) => {
            if(rs.code === 200)
                muyu_noty('修改成功', 'success');
            else
                muyu_noty('修改失败');
        });
    });
    logoutBtn.addEventListener('click', () => {
        muyu_post(api + 'logout', {}, (rs) => {    
            if(rs.code === 200)
            {
                muyu_noty('登出成功', 'success');
                setTimeout(() => {window.parent.location = 'login.html'}, 2000)
            }
            else
                muyu_noty('退出登录失败');
        })
    });
}

function initSetting()
{
    
    muyu_get(api + 'user-info', (rs) => {
        let settingData = rs.data;
        infoPublicSelect.value = settingData.isInfoPublic;
        orderHistoryPublicSelect.value = settingData.isHistoryPublic;
    });   
}
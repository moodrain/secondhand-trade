let user;
let subtypeUserChosen;
let provinceUserChosen;
let cityUserChosen;
let iframe = document.querySelector('#right-content-main');

$(() => {
    initUser();
    initAddress();
    initLeftNav();
    setIframeHeight();
    initRefresh();
    initContentPage();
});

function initRefresh()
{
    let content = document.querySelector('#right-content');
    let isLoaded = true;
    content.addEventListener('scroll', () => {
        if(content.scrollHeight - content.clientHeight - content.scrollTop < 50 && isLoaded)
        {
            if (typeof iframe.contentWindow.loadMore !== "function") return; 
            isLoaded = false;
            iframe.contentWindow.loadMore();
            setTimeout(() => {
                setIframeHeight();
                isLoaded = true;
            }, 1000);
        }
    });
}
function initUser()
{
    muyu_get(api + 'user-info', (rs) => {
        if(rs.code === 200)
        {
            user = rs.data;
            document.querySelector('#avatar').src = user.avatar ? user.avatar.toString().split(' ').join('+') : defaultAvatarUrl;
        }
        else if(rs.code === 401)
            {}
        else
            alert(rs.msg);
        if(user !== undefined)
        {
            let bindClick = (elem, url) => {
                document.querySelector(elem).addEventListener('click', () => {
                    iframe.src = url;
                    setIframeHeight();
                });
            };
            ['#avatar', '#order', '#message', '#setting'].forEach((elem) => {
                document.querySelector(elem).style.display = 'inline-block';
            });
            bindClick('#avatar', 'edit-user-info.html');
            bindClick('#order', 'order.html');
            bindClick('#message', 'message.html');
            bindClick('#setting', 'setting.html');
            bindClick('#publish-or-login', 'publish.html');
            document.querySelector('#publish-or-login').innerHTML = '<p>发 布</p>';
        }
        else
            document.querySelector('#publish-or-login').addEventListener('click', () => {
                location = 'login.html';
            })
    });
}



function initLeftNav()
{
    muyu_get(api + 'type', (rs) => {
        let leftNav = document.querySelector('#left-nav');
        let data = rs.data;
        let newData = [];
        for(let i = 0;i < data.length;i++)
        {
            if(data[i].parent === 0)
            {
                let theNewData = newData[newData.length] = data[i];
                theNewData.children = [];
                for(let j = 0;j < data.length;j++)
                {
                    if(data[j].parent === data[i].id)
                        theNewData.children[theNewData.children.length] = data[j];
                }
            }
        }
        let leftNavData = newData;
        for(let i = 0;i < leftNavData.length;i++)
        {
            let ulData = leftNavData[i];
            let ul = document.createElement('div');
            let title = document.createElement('div');
            let icon = document.createElement('img');
            let titleSpan = document.createElement('span');
            let li = document.createElement('div');
            ul.className = 'left-nav-ul';
            title.className = 'left-nav-ul-title';
            icon.className = 'left-nav-icon';
            li.className = 'left-nav-li';
            for(let j = 0;j < ulData.children.length;j++)
            {
                let a = document.createElement('a');
                a.innerHTML = ulData.children[j].name;
                a.href = 'javascript:void(0)';
                a.addEventListener('click', () => {
                    iframe.src = host + 'index-content.html?type=' + ulData.id + '&subType=' + ulData.children[j].id;
                    setIframeHeight();
                });
                li.appendChild(a);
            }
            titleSpan.innerHTML = ulData.name;
            icon.src = ulData.icon;
            title.appendChild(icon);
            title.appendChild(titleSpan);
            ul.appendChild(title);
            ul.appendChild(li);
            leftNav.appendChild(ul);
        }
    });
}


function setIframeHeight()
{
    let rightContent = document.querySelector('#right-content');
    if(document.body.scrollWidth > 1250)
    {
        setTimeout(() => {
            iframe.style.height = iframe.contentDocument.body.scrollHeight + 'px';
        }, 500);
        setTimeout(() => {
            iframe.style.height = iframe.contentDocument.body.scrollHeight + 'px';
        }, 2000);
    }
    window.addEventListener('resize', () => {
        if(document.body.scrollWidth < 1250) 
        {
            iframe.style.height = document.body.scrollHeight - iframe.offsetTop + 'px';
            iframe.style.overflowX = 'scroll';
        } 
        else 
        {
            iframe.style.height = iframe.contentDocument.body.scrollHeight + 'px';
            iframe.style.overflowX = 'hidden';
        }
    });
}

function initAddress()
{
    let provinceSelect = document.querySelector('#province-select');
    let citySelect = document.querySelector('#city-select');
    let addressSpan = document.querySelector('#address-span');
    let province = muyu_getCookie('provinceChosen');
    let city = muyu_getCookie('cityChosen');
    let spanChange = () => 
    {
        addressSpan.innerHTML = (!province && !city) ? '所有地区' : province.substring(0, province.length - 1) + (city !== '' ? city.substring(0, city.length - 1) : '');
        if(addressSpan.innerHTML.substring(0, 2) === addressSpan.innerHTML.substring(2, 4))
            addressSpan.innerHTML = addressSpan.innerHTML.substring(0,2);
    }
    provinceSelect.addEventListener('change', () => {
        province = provinceSelect.value;
        city = '';
        muyu_setCookie('provinceChosen', province);
        muyu_setCookie('cityChosen', '');
        spanChange();
        iframe.contentWindow.location.reload();
    });
    citySelect.addEventListener('change', () => {
        city = citySelect.value;
        muyu_setCookie('cityChosen', city);
        spanChange();
        iframe.contentWindow.location.reload();
    });
    $('#distpicker-div').distpicker({
        province: province ? province : '不限省份',
        city: city ? city : '不限市区',
    });
    provinceUserChosen = province;
    cityUserChosen = city;
    spanChange();
}

function initContentPage()
{
    let contentUrl = muyu_query_param('content-url');
    if(contentUrl)
        iframe.src = contentUrl;
}
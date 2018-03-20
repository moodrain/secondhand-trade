let page = 0;
let noMoreGoods = false;
$(() => {
    initSlider();
    initNotice();
    initGoods();
});

function loadMore()
{
    if(noMoreGoods) return;
    muyu_get(getGoodsUrl(++page), (rs) => {
        if(rs.data.length === 0)
        {
            noMoreGoods = true;
            muyu_noty('没有更多商品啦', 'default', 'bottom');
        }
        else
            createGoodsElem(rs.data);
    })
}

function initSlider()
{
    let slider = document.querySelector('#slider');
    let sliderData = initSliderData();
    for(let i = 0;i < sliderData.length;i++)
    {
        let divData = sliderData[i];
        let div = document.createElement('div');
        div.style.backgroundImage = 'url(' + divData.img + ')';
        div.addEventListener('click', () => {
            // window.open(divData.href);
        });
        slider.appendChild(div);
    }
    $('#slider').slick({
        dots: true,
        infinite: true,
        speed: 500,
        cssEase: 'linear',
        arrows: false,
        autoplay: true
    });
}

function initNotice()
{
    muyu_get(api + 'notice?page=0&size=4', (data) => {
        let notice = document.querySelector('#notice');
        let noticeData = data.data;
        for(let i = 0;i < noticeData.length;i++)
        {
            let liData = noticeData[i];
            let date = new Date(liData.date);
            let year = date.getFullYear()
            let day = date.getMonth().toString().padStart(2, '0') + '.' + date.getDate();
            let li = document.createElement('li');
            let dateDiv = document.createElement('div');
            let dayP = document.createElement('p');
            let yearP = document.createElement('p');
            let textDiv = document.createElement('div');
            if(i === 0)
                li.className = 'notice-li-first';
            dateDiv.className = 'notice-date';
            textDiv.className = 'notice-text ellipsis2';
            dayP.className = 'notice-date-day';
            yearP.className = 'notice-date-year';
            dayP.innerHTML = day;
            yearP.innerHTML = year;
            textDiv.innerHTML = liData.content;
            li.addEventListener('click', () => {
                $('#notice-modal').modal();
                $('#notice-modal-content').html(liData.content);
            });
            dateDiv.appendChild(dayP);
            dateDiv.appendChild(yearP);
            li.appendChild(dateDiv);
            li.appendChild(textDiv);
            notice.appendChild(li);
        }
    });
}

function initGoods()
{
    muyu_get(getGoodsUrl(0), (rs) => {
        if(rs.data.length === 0)
        {
            noMoreGoods = true;
            muyu_noty('没有更多商品啦', 'default', 'bottom');
        }
        else
            createGoodsElem(rs.data);
    });
}

function createGoodsElem(goodsData)
{
    let goods = document.querySelector('#goods');
    for(let i = 0;i < goodsData.length;i++)
    {
        let divData = goodsData[i];
        let div = document.createElement('div');
        let titleP = document.createElement('p');
        let img = document.createElement('img');
        let priceP = document.createElement('p');
        let priceAddonSpan = document.createElement('span');
        let priceSpan = document.createElement('span');
        let addressSpan = document.createElement('span');
        let hr = document.createElement('hr');
        let dateP = document.createElement('p');
        let fromSpan = document.createElement('span');
        let fromA = document.createElement('a');
        if(i % 4 === 0)
            div.className = 'goods-item goods-item-left';
        else if(i % 4 === 3)
            div.className = 'goods-item goods-item-right';
        else
            div.className = 'goods-item';
        titleP.className = 'goods-item-title';
        img.className = 'goods-item-img';
        priceP.className = 'goods-item-price';
        priceAddonSpan.className = 'goods-item-price-addon';
        addressSpan.className = 'goods-item-address';
        dateP.className = 'goods-item-date';
        fromSpan.className = 'goods-item-from';
        titleP.innerHTML = divData.name;
        img.src = me(divData.img, defaultGoodsImgUrl, null, img => muyu_img_src_base64(img));
        priceAddonSpan.innerHTML = '￥';
        priceSpan.innerHTML = divData.price;
        addressSpan.innerHTML = divData.province.substring(0, divData.province.length - 1) + ' ' + divData.city.substring(0, divData.city.length - 1);
        dateP.innerHTML = muyu_time_str(divData.date / 1000);
        fromSpan.innerHTML = ' 来自 ';
        fromA.innerHTML = divData.user.nick;
        fromA.href = 'javascript:void(0)';
        clickToContentUrl.call(img, host + '?content-url=goods.html?id=' + divData.id, true);
        clickToContentUrl.call(titleP, host + '?content-url=goods.html?id=' + divData.id, true);
        clickToContentUrl.call(fromA, host + '?content-url=user.html?id=' + divData.user.id, true);
        fromSpan.appendChild(fromA);
        dateP.appendChild(fromSpan);
        priceP.appendChild(priceAddonSpan);
        priceP.appendChild(priceSpan);
        priceP.appendChild(addressSpan);
        div.appendChild(titleP);
        div.appendChild(img);
        div.appendChild(priceP);
        div.appendChild(hr);
        div.appendChild(dateP);
        goods.appendChild(div);
    }
}

function getGoodsUrl(page)
{
    let type = muyu_query_param('type');
    let subType = muyu_query_param('subType');
    let province = muyu_getCookie('provinceChosen');
    let city = muyu_getCookie('cityChosen');
    type = type ? type : '';
    subType = subType ? subType : '';
    province = province ? province : '';
    city = city ? city : '';
    return api + 'goods/?type=' + type + '&subType=' + subType + '&province=' + province + '&city=' + city + '&page=' + page + '&size=12';
}

function initSliderData()
{
    return [
        {
            'img': 'static/img/slider/1.png',
            'href': 'slider/1'
        },
        {
            'img': 'static/img/slider/2.png',
            'href': 'slider/2'
        },
        {
            'img': 'static/img/slider/3.png',
            'href': 'slider/3'
        },
        {
            'img': 'static/img/slider/4.png',
            'href': 'slider/4'
        }
    ];
}
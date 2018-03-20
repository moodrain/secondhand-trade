$(() => {
    initGoodsInfo();
});

function initGoodsInfo()
{
    muyu_get(api + 'goods-item?id=' + muyu_query_param('id'), (rs) => {
        let data = rs.data;
        let dateObj = new Date(data.date);
        muyu_fill_inputs({
            'goods-name': data.name,
            'goods-price': data.price,
            'goods-detail': data.detail,
            'goods-date': dateObj.getFullYear() + '-' + dateObj.getMonth().toString().padStart(2, '0') + '-' + dateObj.getDate().toString().padStart(2, '0'),
            'goods-address': data.province + data.city + data.district,
            'goods-user': '<a target="_blank" href="/?content-url=user.html?id=' + data.user.id + '">' + data.user.nick + '</a>',
            'goods-img': me(data.img, defaultGoodsImgUrl, null, img => muyu_img_src_base64(img)),
        });
        initBuyBtn(data.id, data.status);
    })
}

function initBuyBtn(id, status)
{
    let buyBtn = document.querySelector('#buy-btn');
    if(status !== 1)
    {
        buyBtn.disabled = true;
        buyBtn.innerHTML = '已被买走';
    }
    buyBtn.addEventListener('click', () => {
        if(confirm('确认要购买吗？'))
        {
            buyBtn.disabled = true;
            muyu_post(api + 'buy', {id}, (rs) => {
                if(rs.code === 200)
                {
                    muyu_noty('购买成功，请到订单中查看', 'success');
                    buyBtn.innerHTML = '你已下单';
                }
                else if(rs.code === 401)
                {
                    muyu_noty('请先登录');
                    setTimeout(()=>{window.parent.location = 'login.html'}, 2000);
                    return;
                }
                else
                {
                    buyBtn.disabled = false;
                    muyu_noty(rs.msg);
                }
            }); 
        }
    });
}
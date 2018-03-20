$(() => {
    initOrder();
});

function initOrder()
{
    muyu_get(api + 'user-info', (rs) => {
        let user = rs.data;
        muyu_get(api + 'order', (rs) => {
            let table = document.querySelector('#order-table');
            let tableData = rs.data;    
            let initOperationTd = (td, type, status, id) => {
                let nextA = document.createElement('a');
                let cancelA = document.createElement('a');
                nextA.className = 'order-operation-next';
                cancelA.className = 'order-operation-cancel';
                cancelA.innerHTML = '取消';
                if(type === 'buy')
                    nextA.innerHTML = '确认';
                else if(type === 'sell')
                    nextA.innerHTML = status === 1 ? '同意' : '确认';
                cancelA.href = nextA.href = 'javascript:void(0)';
                nextA.addEventListener('click', () => {
                    if(confirm('确认要' + nextA.innerHTML + '订单吗'))
                        muyu_post(api + 'order-next', {id}, (rs) => {
                            if(rs.code === 200)
                            {
                                muyu_noty( nextA.innerHTML + '订单成功', 'success');
                                setTimeout(() => window.location.reload(), 2000);
                            }
                            else
                                muyu_noty(rs.msg);
                        });
                });
                cancelA.addEventListener('click', () => {
                    if(confirm('确认要取消订单吗'))
                        muyu_post(api + 'order-cancel', {id}, (rs) => {
                            if(rs.code === 200)
                            {
                                muyu_noty('取消订单成功', 'success');
                                setTimeout(() => window.location.reload(), 2000);
                            }
                            else
                                muyu_noty(rs.msg);
                        });
                });
                if([5,6,7,8].indexOf(status) === -1)
                {
                    if(type === 'buy')
                    {
                        if(status !== 1 && status !== 4)
                            td.appendChild(nextA);
                    }
                    else if(type === 'sell')
                    {
                        if(status !== 3)
                            td.appendChild(nextA);
                    }
                    td.appendChild(cancelA);
                }        
            };
            let toStatusStr = (type, status) => {
                if(type === 'buy')
                {
                    switch (status) 
                    {
                        case 1: return '买:等待卖家同意';
                        case 2: 
                        case 3: return '买:等待你的确认';
                        case 4: return '买:等待卖家确认';
                        case 5: return '买:订单完成';
                        case 6: return '买:卖家未同意';
                        case 7: return '买:你已取消';
                        case 8: return '买:卖家取消';
                        default: return '未知';
                    }
                }
                else if(type === 'sell')
                {
                    switch (status) 
                    {
                        case 1: return '卖:等待你的同意';
                        case 2: return '卖:等待你的确认';
                        case 3: return '卖:等待买家确认';
                        case 4: return '卖:等待你的确认';
                        case 5: return '卖:订单完成';
                        case 6: return '卖:你未同意';
                        case 7: return '卖:买家取消';
                        case 8: return '卖:你已取消';
                        default: return '未知';
                    }
                }
                else
                    return '未知';
            };
            for(let i = 0;i < tableData.length;i++)
            {
                let trData = tableData[i];
                trData.type = user.id == trData.seller.id ? 'sell' : 'buy';
                let tr = document.createElement('tr');
                let idTd = document.createElement('td');
                let goodsNameTd = document.createElement('td');
                let goodsNameA = document.createElement('a');
                let goodsImgTd = document.createElement('td');
                let goodsImg = document.createElement('img');
                let priceTd = document.createElement('td');
                let userNameTd = document.createElement('td');
                let userNameA = document.createElement('a');
                let statusTd = document.createElement('td');
                let dateTd = document.createElement('td');
                let operationTd = document.createElement('td');
                idTd.innerHTML = trData.id;
                goodsNameA.innerHTML = trData.goods.name;
                goodsNameA.target = '_blank';
                goodsNameA.href = host + '?content-url=goods.html?id=' + trData.goods.id;
                goodsImg.src = me(trData.goods.img, defaultGoodsImgUrl, null, img => muyu_img_src_base64(img));
                goodsImg.className = 'goods-thumbnail';
                priceTd.innerHTML = trData.goods.price;
                userNameA.innerHTML = trData.seller.nick;
                userNameA.target = '_blank';
                userNameA.href = host + '?content-url=user.html?id=' + trData.seller.id;
                statusTd.innerHTML = toStatusStr(trData.type, trData.status);
                let dateObj = new Date(trData.date);
                dateTd.innerHTML = dateObj.getFullYear() + '-' + dateObj.getMonth() + '-' + dateObj.getDate();
                initOperationTd(operationTd, trData.type, trData.status, trData.id);
                tr.appendChild(idTd);
                tr.appendChild(goodsNameTd);
                goodsNameTd.appendChild(goodsNameA);
                tr.appendChild(goodsImgTd);
                goodsImgTd.appendChild(goodsImg);
                tr.appendChild(priceTd);
                tr.appendChild(userNameTd);
                userNameTd.appendChild(userNameA);
                tr.appendChild(statusTd);
                tr.appendChild(dateTd);
                tr.appendChild(operationTd);
                table.appendChild(tr);
            }
        });
    })
}
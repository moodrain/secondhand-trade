let publishLi = document.querySelector('#publish-li');
let listLi = document.querySelector('#list-li');
let updateLi = document.querySelector('#update-li');
let publishBox = document.querySelector('#publish-box');
let listBox = document.querySelector('#list-box');
let updateBox = document.querySelector('#update-box');
let publishSubmitBtn = document.querySelector('#publish-submit-btn');
let updateSubmitBtn = document.querySelector('#update-submit-btn');
let publishEditor;
let updateEditor;
let publishInputs = {
    name: 'publish-name-input',
    price: 'publish-price-input',
    province: 'publish-province-select',
    city: 'publish-city-select',
    district: 'publish-district-select',
    type: 'publish-type-select',
    subType: 'publish-subType-select',
};
let updateInputs = {
    id: 'update-id-input',
    name: 'update-name-input',
    price: 'update-price-input',
    province: 'update-province-select',
    city: 'update-city-select',
    district: 'update-district-select',
    type: 'update-type-select',
    subType: 'update-subType-select',
};

$(() => {
    initTypeSelect();
    initEditor();
    initRightBar();
    initAddress();
    initList();
    initSubmitBtn();
});

function initEditor()
{
    var E = window.wangEditor;
    publishEditor = new E('#publish-editor-toolbar', '#publish-editor');
    updateEditor = new E('#update-editor-toolbar', '#update-editor');
    publishEditor.create();
    updateEditor.create();
}

function initAddress()
{
    let data = initUserInfoData();
    $('#publish-distpicker-div').distpicker({
        province: data.province,
        city: data.city,
        district: data.district,
    });
}

function changeBox(li, box)
{
    publishLi.style.background = 'white';
    listLi.style.background = 'white';
    updateLi.style.background = 'white';
    publishBox.style.display = 'none';
    listBox.style.display = 'none';
    updateBox.style.display = 'none';
    li.style.background = 'lightgray';
    box.style.display = 'block';
}

function initRightBar()
{
    publishLi.addEventListener('click', () => {
        changeBox(publishLi, publishBox);
    });
    listLi.addEventListener('click', () => {
        changeBox(listLi, listBox);
    });
    changeBox(publishLi, publishBox);
}

function initList()
{
    let table = document.querySelector('#list-table');
    let initOperationTd = (td, status, id) => {
        let updateA = document.createElement('a');
        let cancelA = document.createElement('a');
        let updateAonClick = (id) => {
            changeBox(updateLi, updateBox);
            muyu_get(api + 'goods-item?id=' + id, (rs) => {
                let data = rs.data;
                let updateBoxData = {
                    'update-id-input': data.id,
                    'update-name-input': data.name,
                    'update-price-input': data.price,
                    'update-type-select': data.type.id,
                    'update-subType-select': data.subType.id,
                };
                muyu_fill_inputs(updateBoxData);
                updateBoxData['update-id-input'] = id;
                updateEditor.txt.html(updateBoxData['update-editor']);
                $('#update-distpicker-div').distpicker({
                    province: data.province,
                    city: data.city,
                    district: data.district,
                });
                updateEditor.txt.html(data.detail);
            });  
        };
        let cancelAonClick = (id) => {
            if(!confirm('确认要下架吗')) return;
            muyu_post(api + 'cancel-goods', {id}, (rs) => {
                if(rs.code === 200)
                {
                    muyu_noty('下架成功', 'success');
                    setTimeout(() => {window.location.reload()}, 2000);
                }
                else
                    muyu_noty('下架失败');
            })
        }
        updateA.className = 'list-operation-update';
        cancelA.className = 'list-operation-cancel';
        updateA.innerHTML = '修改';
        cancelA.innerHTML = '下架';
        updateA.href = 'javascript:void(0)';
        cancelA.href = 'javascript:void(0)';
        updateA.addEventListener('click', () => {
            updateAonClick(id);
        });
        cancelA.addEventListener('click', () => {
            cancelAonClick(id);
        });
        if(status === 1)
        {
            td.appendChild(updateA);
            td.appendChild(cancelA);
        }
    };
    muyu_get(api + 'my-goods', (rs) => {
        let tableData = rs.data;
        for(let i = 0;i < tableData.length;i++)
        {
            let trData = tableData[i];
            let tr = document.createElement('tr');
            let idTd = document.createElement('td');
            let nameTd = document.createElement('td');
            let nameA = document.createElement('a');
            let priceTd = document.createElement('td');
            let statusTd = document.createElement('td');
            let dateTd = document.createElement('td');
            let operationTd = document.createElement('td');
            idTd.innerHTML = trData.id;
            nameA.innerHTML = trData.name;
            nameA.target = '_blank';
            nameA.href = host + '?content-url=goods.html?id=' + trData.id;
            priceTd.innerHTML = trData.price;
            statusTd.innerHTML = trData.status === 1 ? '已上架' : '交易中';
             let dateObj = new Date(trData.date);
             dateTd.innerHTML = dateObj.getFullYear() + '-' + dateObj.getMonth().toString().padStart(2, '0') + '-' + dateObj.getDate().toString().padStart(2, '0'),
            initOperationTd(operationTd, trData.status, trData.id);
            tr.appendChild(idTd);
            tr.appendChild(nameTd);
            nameTd.appendChild(nameA);
            tr.appendChild(priceTd);
            tr.appendChild(statusTd);
            tr.appendChild(dateTd);
            tr.appendChild(operationTd);
            table.appendChild(tr);
        }
    });

}

function initSubmitBtn()
{
    publishSubmitBtn.addEventListener('click', () => {
        publishSubmitBtn.disabled = true;
        publishSubmitBtn.innerHTML = '发布中...';
        let data = muyu_inputs(publishInputs);
        muyu_file_base64(document.querySelector('#publish-img-input').files[0], (base) => {
            data.img = base;
            data.detail = publishEditor.txt.html();
            muyu_post(api + 'publish', data, (rs) => {
                if(rs.code === 200)
                {
                    muyu_noty('发布成功，请到列表中查看', 'success');
                    publishSubmitBtn.innerHTML = '发布成功';
                    setTimeout(() => {window.location.reload()}, 2000);
                }
                else if(rs.code === 400)
                {
                    muyu_noty(rs.msg);
                    publishSubmitBtn.disabled = false;
                    publishSubmitBtn.innerHTML = '确认发布';
                }
                else
                {
                    muyu_noty(rs.msg, 'danger');
                    publishSubmitBtn.innerHTML = '发布失败';
                }
            })
        });
    });
    updateSubmitBtn.addEventListener('click', () => {
        updateSubmitBtn.disabled = true;
        updateSubmitBtn.innerHTML = '更新中...';
        let data = muyu_inputs(updateInputs);
        muyu_file_base64(document.querySelector('#update-img-input').files[0], (base) => {
            data.img = base;
            data.detail = updateEditor.txt.html();
            muyu_post(api + 'update-goods', data, (rs) => {
                if(rs.code === 200)
                {
                    muyu_noty('更新成功，请到列表中查看', 'success');
                    updateSubmitBtn.innerHTML = '更新成功';
                    setTimeout(() => {window.location.reload()}, 2000);
                }
                else if(rs.code === 400)
                {
                    muyu_noty(rs.msg);
                    updateSubmitBtn.disabled = false;
                    updateSubmitBtn.innerHTML = '确认更新';
                }
                else
                {
                    muyu_noty(rs.msg, 'danger');
                    updateSubmitBtn.innerHTML = '更新失败';
                }
            })
        });
    });
}

function initTypeSelect()
{
    
    muyu_get(api + 'type', (rs) => {
        let publishTypeSelect = document.querySelector('#publish-type-select');
        let publishSubTypeSelect = document.querySelector('#publish-subType-select');
        let updateTypeSelect = document.querySelector('#update-type-select');
        let updateSubTypeSelect = document.querySelector('#update-subType-select');
        let data = rs.data;
        let newData = [];
        for(let i = 0;i < data.length;i++)
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
        data = newData;
        for(let i = 0;i < data.length;i++)
        {
            let option1 = document.createElement('option');
            option1.innerHTML = data[i].name;
            option1.value = data[i].id;
            let option2 = document.createElement('option');
            option2.innerHTML = data[i].name;
            option2.value = data[i].id;
            publishTypeSelect.appendChild(option1);
            updateTypeSelect.appendChild(option2);
        }
        publishTypeSelect.addEventListener('change', () => {
            publishSubTypeSelect.innerHTML = '';
            for(let i = 0;i < data.length;i++)
                if(data[i].id == publishTypeSelect.value)
                    for(let j = 0;j < data[i].children.length;j++)
                    {
                        let option = document.createElement('option');
                        option.innerHTML = data[i].children[j].name;
                        option.value = data[i].children[j].id;
                        publishSubTypeSelect.appendChild(option);
                    }
        });
        for(let i = 0;i < data[0].children.length;i++)
        {
            let option = document.createElement('option');
            option.innerHTML = data[0].children[i].name;
            option.value = data[0].children[i].id;
            publishSubTypeSelect.appendChild(option);
        }
        updateTypeSelect.addEventListener('change', () => {
            updateSubTypeSelect.innerHTML = '';
            for(let i = 0;i < data.length;i++)
                if(data[i].id == updateTypeSelect.value)
                    for(let j = 0;j < data[i].children.length;j++)
                    {
                        let option = document.createElement('option');
                        option.innerHTML = data[i].children[j].name;
                        option.value = data[i].children[j].id;
                        updateSubTypeSelect.appendChild(option);
                    }
        });
        for(let i = 0;i < data.length;i++)
            for(let j = 0;j < data[i].children.length;j++)
            {
                let option = document.createElement('option');
                option.innerHTML = data[i].children[j].name;
                option.value = data[i].children[j].id;
                updateSubTypeSelect.appendChild(option);
            } 
    });
}

function initUserInfoData()
{
    return {
        'province': '广东省',
        'city': '广州市',
        'district': '番禺区'
    };
}

function initListData()
{
    return [
        {
            'id': 10001,
            'name': '超级无敌苹果手机',
            'price': 998,
            'date': '2018-12-12',
            'status': 0
        },
        {
            'id': 10002,
            'name': '超级无敌安卓手机',
            'price': 998,
            'date': '2018-12-12',
            'status': 1
        }
    ];   
}
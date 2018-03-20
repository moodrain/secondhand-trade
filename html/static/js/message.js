$(() => {
    initRightBar();
    initMessage();
});

function initRightBar()
{
    let messageBox = document.querySelector('#message-box');
    let noticeBox = document.querySelector('#notice-box');
    let messageLi = document.querySelector('#message-li');
    let noticeLi = document.querySelector('#notice-li');
    messageLi.style.background = 'lightgray';
    let show = (li, box) => {
        messageLi.style.background = 'white';
        noticeLi.style.background = 'white';
        messageBox.style.display = 'none';
        noticeBox.style.display = 'none';
        li.style.background = 'lightgray';
        box.style.display = 'block';
    };
    messageLi.addEventListener('click', () => {
        show(messageLi, messageBox);
    });
    noticeLi.addEventListener('click', () => {
        show(noticeLi, noticeBox);
    });
}

function initMessage()
{
    muyu_get(api + 'messages', (rs) => {
        let messageAndNoticeData = rs.data.data;
        let userId = rs.data.id;
        let noticeData = [];
        let messageData = [];
        messageAndNoticeData.forEach((item) => {
            if(item.isSystem)
                noticeData.push(item);
            else
                messageData.push(item);
        });
        createNotice(noticeData, userId);
        createMessage(messageData, userId);
    });
}

function createMessage(data, userId)
{
    let messageUl = document.querySelector('#message-ul'); 
    let uniqueMessage = [];
    let uniqueMessageFrom = [];
    for(let i = 0;i < data.length;i++)
    {
        let messageTmp = data[i];
        if(uniqueMessageFrom.includes(data[i].from.id))
            continue;
        if(data[i].from.id === userId)
            continue;
        for(let j = i;j < data.length;j++)
            if(data[j].from.id == data[i].from.id)
                if(data[j].id > data[i].id)
                    messageTmp = data[j];
        uniqueMessageFrom.push(data[i].from.id);
        uniqueMessage.push(data[i]);
    }
    let messageData = uniqueMessage;
    for(let i = 0;i < messageData.length;i++)
        for(let j = 0;j < data.length;j++)
            if(data[j].from.id === messageData[i].to.id)
                if(data[j].id > messageData[i].id)
                    messageData[i].responsed = true;
    for(let i = 0;i < messageData.length;i++)
    {
        let liData = messageData[i];
        let li = document.createElement('li');
        let p = document.createElement('p');
        let avatarImg = document.createElement('img');
        let fromA = document.createElement('a');
        let contentSpan = document.createElement('span');
        let dateSpan = document.createElement('span');
        let responseA = document.createElement('a');
        dateSpan.className = 'message-date';
        dateSpan.innerHTML = muyu_time_str(liData.date / 1000);
        if(liData.responsed)
        {
            responseA.className = 'message-responsed';
            responseA.innerHTML = '已回';
        }
        else
        {
            responseA.href = host + 'user.html?section=chat&id=' + liData.from.id;
            responseA.className = 'message-response';
            responseA.innerHTML = '回复';
        }
        contentSpan.className = 'message-content';
        contentSpan.innerHTML = '：' + liData.content;
        avatarImg.src = liData.from.avatar ? muyu_img_src_base64(liData.from.avatar) : defaultAvatarUrl;
        fromA.innerHTML = liData.from.nick;
        fromA.className = 'message-nick';
        fromA.href = host + 'user-info.html?id=' + liData.from.id;
        p.appendChild(avatarImg);
        p.appendChild(fromA);
        p.appendChild(contentSpan);
        p.appendChild(responseA);
        p.appendChild(dateSpan);
        li.appendChild(p);
        messageUl.appendChild(li);
    }
}

function createNotice(noticeData, userId)
{
    let noticeUl = document.querySelector('#notice-ul');
    for(let i = 0;i < noticeData.length;i++)
    {
        let liData = noticeData[i];
        if(liData.to.id !== userId) continue;
        let li = document.createElement('li');
        let p = document.createElement('p');
        let avatarImg = document.createElement('img');
        let fromA = document.createElement('a');
        let contentSpan = document.createElement('span');
        let dateSpan = document.createElement('span');
        dateSpan.className = 'message-date';
        dateSpan.innerHTML = muyu_time_str(liData.date / 1000);
        contentSpan.className = 'message-content';
        contentSpan.innerHTML = '：' + liData.content;
        avatarImg.src = me(liData.from, 'static/img/icon/remind.png', from => from.id !== 0, from => muyu_img_src_base64(me(from.avatar, defaultAvatarUrl)));
        fromA.innerHTML = me(liData.from, '系统', from => from.id !== 0, from => from.nick);
        fromA.className = 'message-nick';
        if(liData.from.id !== 0)
        {
            fromA.target = '_blank';
            fromA.href = host + '?content-url=user.html?id=' + liData.from.id;
        }
        p.appendChild(avatarImg);
        p.appendChild(fromA);
        p.appendChild(contentSpan);
        p.appendChild(dateSpan);
        li.appendChild(p);
        noticeUl.appendChild(li);
    }
}
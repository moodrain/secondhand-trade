let infoLi = document.querySelector('#info-li');
let chatLi = document.querySelector('#chat-li');
let infoBox = document.querySelector('#info-box');
let chatBox = document.querySelector('#chat-box');
let infoDiv = document.querySelector('#info-div');
let infoNotPublicDiv = document.querySelector('#info-not-public-div');
let chatInited = false;
let messageIdLoaded = 0; //用于拉取新消息时进行版本对比

$(() => {
    initRightBar();
    initUserInfo();
});

function changeBox(li, box)
{
    infoLi.style.background = 'white';
    chatLi.style.background = 'white';
    infoBox.style.display = 'none';
    chatBox.style.display = 'none';
    li.style.background = 'lightgray';
    box.style.display = 'block';
    if(li === chatLi && !chatInited)
    {
        initChat();
        initSendBtn();
        chatInited = true;
    }
}

function initRightBar()
{
    infoLi.addEventListener('click', () => {
        changeBox(infoLi, infoBox);
    });
    chatLi.addEventListener('click', () => {
        changeBox(chatLi, chatBox);
    });
    if(muyu_query_param('section') === 'chat')
        changeBox(chatLi, chatBox);
    else
        changeBox(infoLi, infoBox);
}

function initUserInfo()
{
    muyu_get(api + 'user?id=' + muyu_query_param('id'), (rs) => {
        if(rs.code === 401)
        {
            muyu_noty('请先登录');
            setTimeout(()=>{window.parent.location = 'login.html'}, 2000);
            return;
        }
        let data = rs.data;
        let userInfo = rs.data.userInfo;
        document.querySelector('#avatar').src = userInfo.avatar ? userInfo.avatar.toString().split(' ').join('+') : defaultAvatarUrl;
        if(userInfo.isInfoPublic)
        {
            infoDiv.style.display = 'block';
            infoNotPublicDiv.style.display = 'none';
            ['nick', 'name', 'age', 'phone', 'mail', 'qq', 'wx', 'introduction'].forEach((elem) => {
                document.querySelector('#' + elem).innerHTML = userInfo[elem];
            });
            muyu_fill_inputs({
                sex: userInfo.sex == null ? '未知' : (sex == 1 ? '男' : '女'),
                address: muyu_express(userInfo.province + userInfo.city + userInfo.district, '', val => val == 0),
            });
        }
        else
        {
            infoDiv.style.display = 'none';
            infoNotPublicDiv.style.display = 'block';
        }
        document.querySelector('#history').innerHTML = userInfo.isHistoryPublic ? data.historyOrder : '保密';
    });
}
function initChat()
{
    loadMessage();
    setInterval(() => {loadMessage(messageIdLoaded)}, 5 * 1000);
}

function loadMessage()
{
    let userId = muyu_query_param('id'); // 注意这个userId是对方的id
    let url = api + 'chat-messages?id=' + userId + '&messageId=' + messageIdLoaded;
    muyu_get(url, (rs) => {
        let data = rs.data;
        for(let i = 0;i < data.length;i++)
        {
            let msg = data[i];
            let type = msg.to.id == userId ? 'send' : 'receive';
            createMsg(msg, type);
            messageIdLoaded = msg.id > messageIdLoaded ? msg.id : messageIdLoaded;
        }
    });
}

function initSendBtn()
{
    let btn = document.querySelector('#send-btn');
    let textarea = document.querySelector('#send-textarea');
    muyu_enter(btn);
    muyu_get(api + 'user-info', (rs) => {
        let user = rs.data;
        btn.addEventListener('click', () => {
            if(!textarea.value) return;
            muyu_post(api + 'send-message', {
                id: muyu_query_param('id'),
                content: textarea.value,
            }, (rs) => {
                if(rs.code === 200)
                {
                    loadMessage();
                    textarea.value = '';
                }
                else
                    muyu_noty(rs.msg);
            });
        });
    })
}

function createMsg(msg, type)
{
    let chatWindow = document.querySelector('#chat-window');
    let p = document.createElement('p');
    let nickSpan = document.createElement('span');
    let dateSpan = document.createElement('span');
    let textSpan = document.createElement('span');
    p.className = 'chat-' + type;
    nickSpan.className = 'chat-nick';
    dateSpan.className = 'chat-date';
    textSpan.className = 'chat-content';
    nickSpan.innerHTML = msg.from.nick + '：';
    dateSpan.innerHTML = muyu_time_str(msg.date / 1000);
    textSpan.innerHTML = msg.content;
    let arr = [nickSpan, dateSpan, textSpan]
    arr = type === 'send' ? arr.reverse() : arr;
    arr.forEach(elem => p.appendChild(elem));
    chatWindow.appendChild(p);
    chatWindow.scrollTop = chatWindow.scrollHeight;
}
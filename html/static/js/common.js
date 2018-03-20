let host = 'http://www.app.com/';
let api = host + 'api/';
let defaultAvatarUrl = 'static/img/avatar.png';
let defaultGoodsImgUrl = 'static/img/goods.png';

function clickToContentUrl(url, openWindow = false){
    this.addEventListener('click', () => {
        if(openWindow)
            window.open(url);
        else
            window.location = url;
        window.parent.setIframeHeight();
    });
}
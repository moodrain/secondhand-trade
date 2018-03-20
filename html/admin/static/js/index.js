let iframe = document.querySelector('#iframe');
layui.use('element', function(){
    var element = layui.element;
});
['goods', 'type', 'order', 'user', 'notice'].forEach(key => {
    let elem = document.querySelector('#' + key + '-dd');
    elem.addEventListener('click', () => {
        iframe.src = key + '.html';
    });
});

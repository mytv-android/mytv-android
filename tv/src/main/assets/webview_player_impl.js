const ___startTime = Date.now();

function getVideoParentShadowRoots() {
    const allElements = document.querySelectorAll('*');
    for (const element of allElements) {
        const shadowRoot = element.shadowRoot;
        if (shadowRoot) return shadowRoot.querySelector('video');
    }
    return null;
}

function removeVideoPlayerControl() {
    const selectors = [
        '#control_bar_player',
        '#pic_in_pic_player',
        '.con.poster',
        'xg-controls',
        '.xgplayer-controls',
        '[data-kp-role=bottom-controls]',
        '.prism-controlbar',
        '.vjs-control-bar',
        '.playback-layer',
        '.control-bar',
        '.bitrate-layer',
        '.volume-layer',
        '.dplayer-controller',
        '._tdp_contrl'
    ];
    selectors.forEach(selector => {
        document.querySelectorAll(selector).forEach(element => {
            element.remove();
        });
    });
}

function removeAllDivElements() {
    
    [...document.body.children].forEach((element) => {
        const tagName = element.tagName.toLowerCase()
        if (tagName != 'script' && tagName != 'video'){
            // element.remove();
            element.style.display = 'none';
        } 
    })
}

function addVideoPlayerMask(video) {
    clearInterval(my_pollingIntervalId);
    document.body.appendChild(video);
    removeAllDivElements();
    video.style = 'width: 100%; height: 100%;object-fit: contain;'
    video.autoplay = true
    document.body.style = 'width: 100vw; height: 100vh; margin: 0; min-width: 0; background: #000; padding: 0;'
    Android.changeVideoResolution(1920, 1080);
}

function enableVideo(video) {
    if (video.muted || video.volume != 1 || video.autoplay === false) {
        video.muted = false;
        video.autoplay = true;
        video.volume = 1;
    }else{
        clearInterval(enableVideo);
    }
}

function cleanAllStyle() {
    const styles = document.querySelectorAll('style,link[rel="stylesheet"]');
    styles.forEach(style => {
        style.remove();
    });
    const allElements = document.querySelectorAll('*');
    allElements.forEach(element => {
        element.removeAttribute('style');
    });
}

function __initializetMain() {
    let video = document.querySelector('video');
    video = video ? video : getVideoParentShadowRoots();
    if (Date.now() - ___startTime > 15000) {
        clearInterval(my_pollingIntervalId);
        try {
            video.pause();
        } catch (error) {
            console.error('Error pausing video:', error);
        }
        Android.updatePlaceholderVisible(true,'加载失败');
        return;
    }
    if (video && video.src) {
        console.info(video.src);
        if (video.paused) video.play();
        video.volume = 1;
        video.muted = false;
        if (video.videoWidth * video.videoHeight !== 0) addVideoPlayerMask(video);
        setInterval(enableVideo, 100, video); //2秒后再看一下
    }
 }
// cleanAllStyle();
const my_pollingIntervalId = setInterval(__initializetMain, 100);
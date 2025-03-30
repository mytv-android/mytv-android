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
        if (element.tagName.toLowerCase() == 'div') {
            console.info(element.innerHTML);
            element.remove()
        }
    })
}

function addVideoPlayerMask(video) {
    clearInterval(my_pollingIntervalId);
    document.body.appendChild(video);
    removeAllDivElements();
    video.style = 'width: 100%; height: 100%;object-fit: contain;'
    video.muted = false;
    video.volume = 1
    video.autoplay = true
    document.body.style = 'width: 100vw; height: 100vh; margin: 0; min-width: 0; background: #000;'
    Android.changeVideoResolution(1920, 1080);
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
        if (video.videoWidth * video.videoHeight !== 0) addVideoPlayerMask(video);
    }
 }

const my_pollingIntervalId = setInterval(__initializetMain, 100);
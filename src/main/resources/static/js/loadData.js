let loading = false;
let currentPage  = 0;
let lastPageReached = false;

function fetchData() {
    if (!loading && !lastPageReached) {
        // 데이터를 가져오는 중임을 표시하는 로딩 인디케이터를 활성화 가능
        loading = true;

        // URLSearchParams 를 사용하여 쿼리 문자열을 파싱
        const queryString = window.location.search;

        const urlParams = new URLSearchParams(queryString);

        // groupName 및 info 파라미터 값을 가져옴
        const groupNameParam = urlParams.get('groupName');
        const infoParam = urlParams.get('info');
        const orderParam = urlParams.get('orderCondition');

        let url = `/rest/search?page=${currentPage}`;

        // groupName 또는 info 파라미터가 존재하면 추가
        if (groupNameParam || infoParam || orderParam) {
            url += `&groupName=${groupNameParam || ''}&info=${infoParam || ''}&orderCondition=${orderParam || ''}`;
        }

        fetch(url)
            .then(response => response.json())
            .then(data => {
                console.log(data.content)
                if (data.content.length === 0) {
                    // 마지막 페이지에 도달한 경우
                    lastPageReached = true;
                } else {
                    updateUI(data);
                    currentPage++; // 다음에 가져올 페이지로 이동
                }
                loading = false; // 로딩 완료 후 상태를 false로 설정
            })
            .catch(error => {
                console.error('데이터를 가져오는 중 오류 발생:', error);
                loading = false; // 오류 발생 시도 로딩 상태를 false로 설정
            });
    }
}
function updateUI(data) {
    const container = document.getElementById('data-container');

    if (Array.isArray(data.content)) {
        data.content.forEach(club => {
            const listItem = createListItem(club);
            container.appendChild(listItem);
        });
    } else {
        console.error('서버에서 예상하지 않은 형태의 데이터를 받았습니다:', data);
        // 예외 처리 또는 적절한 조치를 취할 수 있습니다.
    }
}

document.addEventListener('DOMContentLoaded', function() {
    fetchData();
});

window.addEventListener('scroll', function() {
    if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 100) {
        fetchData();
    }
});

function createListItem(club) {

    const listItem = document.createElement('li');
    // li 요소에 클릭 이벤트 처리
    listItem.onclick = function() {
        location.href = `/groups/group?groupId=${club.groupId}`;
    };

    const wrapperDiv = document.createElement('div');
    wrapperDiv.classList.add('w-full');

    // 이미지 부분 추가
    const imageSpan = document.createElement('span');
    imageSpan.classList.add('groups-img-box-gra');

    const imageBoxParagraph = document.createElement('p');
    imageBoxParagraph.classList.add('groups-img-box');

    const image = document.createElement('img');
    image.classList.add('groups-img');
    image.src = club.storeFileName ? `/upload/${club.storeFileName}` : '/images/noImage.jpg';
    image.alt = club.groupName;

    imageBoxParagraph.appendChild(image);
    imageSpan.appendChild(imageBoxParagraph);

    // 하트 아이콘 및 좋아요 수 추가
    const heartSpan = document.createElement('span');
    const heartSvg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
    heartSvg.classList.add('heart');
    heartSvg.setAttribute('viewBox', '0 0 512 512');

    const heartPath = document.createElementNS('http://www.w3.org/2000/svg', 'path');
    heartPath.setAttribute('d', 'M0 190.9V185.1C0 115.2 50.52 55.58 119.4 44.1C164.1 36.51 211.4 51.37 244 84.02L256 96L267.1 84.02C300.6 51.37 347 36.51 392.6 44.1C461.5 55.58 512 115.2 512 185.1V190.9C512 232.4 494.8 272.1 464.4 300.4L283.7 469.1C276.2 476.1 266.3 480 256 480C245.7 480 235.8 476.1 228.3 469.1L47.59 300.4C17.23 272.1 .0003 232.4 .0003 190.9L0 190.9z');

    const likeCountSpan = document.createElement('span');
    const likeCountText = document.createTextNode(club.likeCount);
    likeCountSpan.appendChild(likeCountText);

    heartSvg.appendChild(heartPath);
    heartSpan.appendChild(heartSvg);
    heartSpan.appendChild(likeCountSpan);

    // 회원 가입 아이콘 및 가입 수 추가
    const joinSpan = document.createElement('span');
    const joinSvg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
    joinSvg.setAttribute('xmlns', 'http://www.w3.org/2000/svg');
    joinSvg.setAttribute('viewBox', '0 0 448 512');
    joinSvg.setAttribute('width', '18px');
    joinSvg.setAttribute('height', '18px');

    const joinPath = document.createElementNS('http://www.w3.org/2000/svg', 'path');
    joinPath.setAttribute('d', 'M224 256c70.7 0 128-57.31 128-128s-57.3-128-128-128C153.3 0 96 57.31 96 128S153.3 256 224 256zM274.7 304H173.3C77.61 304 0 381.6 0 477.3c0 19.14 15.52 34.67 34.66 34.67h378.7C432.5 512 448 496.5 448 477.3C448 381.6 370.4 304 274.7 304z');
    joinPath.setAttribute('fill', 'gray');

    const joinCountSpan = document.createElement('span');
    const joinCountText = document.createTextNode(club.joinCount);
    joinCountSpan.appendChild(joinCountText);

    joinSvg.appendChild(joinPath);
    joinSpan.appendChild(joinSvg);
    joinSpan.appendChild(joinCountSpan);

    // bottom 부분 추가
    const bottomParagraph = document.createElement('p');
    bottomParagraph.classList.add('bottom');
    bottomParagraph.appendChild(heartSpan);
    bottomParagraph.appendChild(joinSpan);

    wrapperDiv.appendChild(imageSpan);
    wrapperDiv.appendChild(bottomParagraph);

    // group 정보 추가
    const groupInfoDiv = document.createElement('div');

    const groupInfoParagraph1 = document.createElement('p');
    groupInfoParagraph1.classList.add('font-base', 'text-center');
    groupInfoParagraph1.textContent = club.groupName;

    const groupInfoParagraph2 = document.createElement('p');
    groupInfoParagraph2.classList.add('group-info');
    groupInfoParagraph2.textContent = club.info;

    groupInfoDiv.appendChild(groupInfoParagraph1);
    groupInfoDiv.appendChild(groupInfoParagraph2);

    const innerDiv = document.createElement('div');
    innerDiv.appendChild(groupInfoDiv);

    listItem.appendChild(wrapperDiv);
    listItem.appendChild(innerDiv);

    return listItem;
}
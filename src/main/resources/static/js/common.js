let originalImageSrc; // 이전 이미지의 URL을 저장할 변수
function displayImage(input) {
    const submitBtn = document.getElementById('submitBtn');
    const cancelBtn = document.getElementById('cancelBtn');

    // submitBtn이 존재하는지 확인하고 스타일을 변경
    if (submitBtn) {
        submitBtn.style.display = 'inline-block';
    }

    const preview = document.getElementById('profileImage');
    const file = input.files[0];

    if (file) {
        const reader = new FileReader();

        reader.onload = function (e) {
            originalImageSrc = preview.src;
            preview.src = e.target.result;
        };

        reader.readAsDataURL(file);
    } else {
        // 파일이 선택되지 않았을 때, 기본 이미지로 변경하거나 다른 작업을 수행할 수 있습니다.
        preview.src = '/images/defaultUser.png';
    }

    // cancelBtn이 존재하는지 확인하고 스타일을 변경
    if (cancelBtn) {
        cancelBtn.style.display = 'inline-block';
        cancelBtn.addEventListener('click', function () {
            // 취소 버튼을 누르면 이전 이미지로 복원
            if (originalImageSrc) {
                preview.src = originalImageSrc;
                document.getElementById('submitBtn').style.display = 'none';
                document.getElementById('cancelBtn').style.display = 'none';
            }
        });
    }
}

function toggleLike(element) {

    const content = $('.likes').data('content');
    const contentId = $('.likes').data('content-id');
    const heartIcon = $('.heart-icon');
    let likeCount;

    $.ajax({
        type: 'POST',
        url: '/rest/like',
        data: {
            contentId: contentId,
            content: content
        },
        // response 1: 좋아요 2: 취소
        success: function (response) {
            // 공통 - 색바꾸기
            heartIcon.toggleClass('active');

            // 1: 좋아요시
            if (response == 1) {
                toastr.success("좋아요를 눌렀습니다!");
                likeCount = parseInt($('#like-count').text());
                likeCount++;
                $('#like-count').text(likeCount);
            } else {
                toastr.success("좋아요를 취소했습니다!");
                likeCount = parseInt($('#like-count').text());
                likeCount--;
                $('#like-count').text(likeCount);
            }

        },
        error: function (error) {
            // 오류 응답 처리
            console.log("에러발생");
        }
    });
}

/* file */
function openFile() {
    $("#input_usr_file").trigger("click");
}


/* 첨부파일 검증 */
function validation(obj) {
    const fileTypes = ['image/gif', 'image/jpeg', 'image/png'];
    if (obj.name.length > 100) {
        alert("파일명이 100자 이상인 파일은 제외되었습니다.");
        return false;
    } else if (obj.size > (10 * 1024 * 1024)) {
        alert("aws 메모리 관계로 5mb이하의 파일을 첨부해주십시오.");
        return false;
    } else if (obj.name.lastIndexOf('.') == -1) {
        alert("확장자가 없는 파일은 제외되었습니다.");
        return false;
    } else if (!fileTypes.includes(obj.type)) {
        alert("첨부가 불가능한 파일은 제외되었습니다.");
        return false;
    } else {
        return true;
    }
}


function validateAndSubmit() {
    const categorySelect = document.getElementById("category");
    if (categorySelect.value === "") {
        layer_open('sendPop', 'send_Pop');
        $("#popContOne").show();
        return;
    }
    // 여기에 필요한 경우 추가적인 유효성 검사 로직을 구현할 수 있습니다.

    // 선택이 올바르다면 폼 제출
    document.getElementById("frm").submit();
}


function layer_open(element) {

    const layer_id = 'send_Pop';
    const pop_id = 'popContOne';
    let joinId = element.getAttribute('data-join-id');
    let groupId = element.getAttribute('data-group-id');

    var temp = $('#' + layer_id);
    var bg = temp.prev().hasClass('bg');	//dimmed 레이어를 감지하기 위한 boolean 변수

    if (bg) {
        $('#' + pop_id).fadeIn();	//'bg' 클래스가 존재하면 레이어가 나타나고 배경은 dimmed 된다.
    } else {
        temp.fadeIn();
    }

    temp.find('a.cbtn').click(function (e) {
        temp.fadeOut();
        e.preventDefault();
    });

    //확인 눌렀을때, 매니저쪽으로 전송
    temp.find('a.mbtn').click(function (e) {

        // Fetch API를 사용한 비동기 호출
        fetch(`/groups/group/member/setManager?joinId=${joinId}&groupId=${groupId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(data => {
                window.location.reload();
            })
            .catch(error => {
                console.error('Error saving comment:', error);
                alert("요청이 실패했습니다");
            });
    });

    $('.bg').click(function (e) {	//배경을 클릭하면 레이어를 사라지게 하는 이벤트 핸들러
        temp.fadeOut();
        e.preventDefault();
    });
}


function addComment() {
    const commentInput = document.querySelector('.comment-input');
    const commentText = commentInput.value;
    const boardId = document.getElementById('boardId').value;

    if (commentText.trim() !== '') {
        // Fetch API를 사용한 비동기 호출
        fetch('/rest/comment', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                content: commentText,
                boardId: boardId
            }),
        })
            .then(response => response.text())
            .then(data => {
                // 성공적으로 댓글이 저장된 후의 처리
                const commentsContainer = document.querySelector('.comments');
                const newComment = document.createElement('div');
                newComment.classList.add('comment');
                newComment.innerHTML = `<p class="comment-user">${data}:</p> <p class="font-content">${commentText}</p>`;
                commentsContainer.appendChild(newComment);

                // 댓글 작성 후 입력 폼 초기화
                commentInput.value = '';
            })
            .catch(error => {
                console.error('Error saving comment:', error);
            });
    }
}

function toggleList() {
    // listContainer 요소를 가져옴
    var listContainer = document.getElementById('listContainer');

    // listContainer의 현재 표시 상태를 확인하여 토글
    if (listContainer.style.display === 'none') {
        listContainer.style.display = 'block'; // 펼치기
    } else {
        listContainer.style.display = 'none'; // 접기
    }
}

function searchByGroupName() {
    const text = document.getElementById('groupNameInput').value;
    const selectedId = document.getElementById('select')
    const selected = selectedId !== null ? selectedId.value : "name";

    const params = new URLSearchParams();


    console.log(selected);

    if (selected == "name") {
        params.append('groupName', encodeURIComponent(text));
    } else {
        params.append('info', encodeURIComponent(text));
    }

    const url = `/groups?${params.toString()}`;

    // 새로운 URL을 사용하여 페이지 이동
    window.location.href = url;
}

// 메뉴 오픈
$(document).ready(function() {
    $(".menu").click(function(){
        $(".head").toggleClass("active");
        $(".head_mg").toggleClass("active");
        $(".menu_box").toggleClass("active");
    });
});

// 메뉴 슬라이드 업 다운
$(document).ready(function() {
    $(".menu_box .down_btn").click(function(){
        $(this).parent().next(".dep2").slideToggle(300);
        $(this).parent().parent("li").toggleClass("active");
    });
});

// 검색 오픈
$(document).ready(function() {
    $("header .search").click(function(){
        $(".search_box").slideToggle(200);
        $(".search_box").toggleClass("active");
    });
});

// 검색 닫기
$(document).ready(function() {
    $(".search_box .x, .search_box a.icon").click(function(){
        $(".search_box").slideUp(200);
        $(".search_box").removeClass("active");
    });
});

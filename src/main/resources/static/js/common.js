function displayImage(input) {
    const preview = document.getElementById('profileImage');
    const file = input.files[0];

    if (file) {
        const reader = new FileReader();

        reader.onload = function (e) {
            preview.src = e.target.result;
        };

        reader.readAsDataURL(file);
    } else {
        // 파일이 선택되지 않았을 때, 기본 이미지로 변경하거나 다른 작업을 수행할 수 있습니다.
        preview.src = '/images/defaultUser.png';
    }
}

/* main slide */
document.addEventListener('DOMContentLoaded', function () {
    const mainNotice = document.querySelector('.main-notice');
    const slickSlides = document.querySelectorAll('.slick-slide');
    let currentSlide = 0;

    function nextSlide() {
        currentSlide = (currentSlide + 1) % slickSlides.length;
        updateSlider();
    }

    function updateSlider() {
        const translateValue = -currentSlide * 100 + '%';
        mainNotice.style.transform = 'translateX(' + translateValue + ')';
    }

    // Adjust the interval duration as needed
    const interval = setInterval(nextSlide, 1300);

    // Pause on hover
    mainNotice.addEventListener('mouseenter', function () {
        clearInterval(interval);
    });

    mainNotice.addEventListener('mouseleave', function () {
        interval = setInterval(nextSlide, 1300);
    });
});
/* main end*/

/* file */
function openFile(){
    $("#input_usr_file").trigger("click");
}

var fileNo = 0;
var filesArr = new Array();
var img;
var maxFileCnt = 5;   // 첨부파일 최대 개수

/* 첨부파일 추가 */
function addFile(obj){
    var attFileCnt = document.querySelectorAll('.filebox').length;    // 기존 추가된 첨부파일 개수
    var delFileCnt = document.querySelectorAll('.delfile').length; // 삭제된 개수
    var remainFileCnt = maxFileCnt - attFileCnt + delFileCnt;    // 추가로 첨부가능한 개수 (최대 파일 - 현재첨부개수 + 삭제된 수)
    var curFileCnt = obj.files.length;  // 현재 선택된 첨부파일 개수

    // 첨부파일 개수 확인
    if (curFileCnt > remainFileCnt) {
        alert("첨부파일은 최대 " + maxFileCnt + "개 까지 첨부 가능합니다.");
    }

    for (var i = 0; i < Math.min(curFileCnt, remainFileCnt); i++) {

        const file = obj.files[i];
        //동기화문제 해결 필요 할 수 있음
        if (validation(file)) {
            // 파일 배열에 담기
            var reader = new FileReader();
            reader.onload = function (e) {
                img = e.target.result;
                var html =
                    '<li id="file' + fileNo + '" class="filebox">'
                    +'<span><img id="preview"width="65" height="65" src="'
                    + img
                    +'")no-repeat 50% 50% / cover"></span>'
                    +'<a onclick="deleteFile(' + fileNo + ');" class="close"></a>'+
                    '</li>';
                $(".file_wrap").append(html);
                filesArr.push(file);
                fileNo++;
                count('plus');
            };
            reader.readAsDataURL(file);

        } else {
            continue;
        }
    }
    // 초기화
    document.querySelector("input[type=file]").value = "";
}




/* 첨부파일 검증 */
function validation(obj){
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

/* 첨부파일 삭제 */
function deleteFile(num) {
    document.querySelector("li#file" + num).remove();
    filesArr[num].is_delete = true;
    count('minus');
}

function delfile(index){
    var file_id = "#file_id_"+index;
    $(file_id).hide();

    var del_id = "#"+index+"_del";
    $(del_id).val('Y');
    $(del_id).addClass('delfile');
    count('minus');
}


/* 폼 전송 */

var isRun = false;
function submitForm(mode) {
    if(isRun == true){ // 중복실행방지
        layer_open('loadPop','load_Pop');
        return;
    }
    isRun = true;
    $('#mode').val(mode);
    if (isSubmit()) {
        loading("판매글을 등록 중입니다...");
        // 폼데이터 담기
        var form = document.querySelector("frm");
        var formData = new FormData(frm);
        for (var i = 0; i < filesArr.length; i++) {
            // 삭제되지 않은 파일만 폼데이터에 담기
            if (!filesArr[i].is_delete) {
                formData.append("attach_file", filesArr[i]);
            }
        }
        $.ajax({
            type: "POST",
            url: "/trade/tradeProc.do",
            data: formData,
            contentType: false,               // * 중요 *
            processData: false,               // * 중요 *
            enctype : 'multipart/form-data',  // * 중요 *
            success: function (data) { // data    1: 등록/수정 완료    2: 거래완료 / 삭제완료
                isRun = false;
                location.href="/trade/tradeList.do";
            },
            error: function (xhr, desc, err) {
                alert('에러가 발생 하였습니다.');
                return;
            }
        });
    }else{
        isRun = false;
        return;
    }
}
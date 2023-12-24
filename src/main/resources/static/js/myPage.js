
document.getElementById('submitBtn').style.display = 'none';
document.getElementById('cancelBtn').style.display = 'none';


function openImg() {

    document.getElementById('input_usr_img').click();
}

document.getElementById('fileForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const formData = new FormData(this);

    console.log("진입했다.")

    // 여기서 서버로 formData를 전송하는 AJAX 요청을 보낼 수 있습니다.
    // 예를 들면, fetch 또는 jQuery의 $.ajax를 사용할 수 있습니다.
    // 서버 측에서는 파일을 받아서 저장하는 로직을 구현해야 합니다.
    // 아래는 fetch를 사용한 예시입니다.

    fetch('/member/mypage/changeImage', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            // 서버로부터의 응답 처리
            console.log(data);
        })
        .catch(error => console.error('Error:', error));
});
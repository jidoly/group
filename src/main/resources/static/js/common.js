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
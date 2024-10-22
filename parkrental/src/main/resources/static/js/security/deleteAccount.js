function deleteAccount() {
    if (confirm("정말로 회원을 탈퇴하시겠습니까?")) {
        fetch('user/deleteAccount', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include'
        })
        .then(response => response.json())
        .then(data => {
            alert(data.message);
            if (data.message === "회원 탈퇴가 완료되었습니다.") {
                windows.location.href = '/';
            }
        })
        .catch(error => {
            console.error('Error: ', error);
            alert('회원 탈퇴 중 오류가 발생했습니다. 다시 시도해 주세요.');
        })
    }
}
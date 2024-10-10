function login(username, password) {
    return fetch('http://localhost:8081/guest/login',{
        method: 'POST',
        headers: {
            'Content-Type':'application/json'
        },
        body: JSON.stringify({username, password})
    })
    .then(response => {
        if(!response.ok) {
            throw new Error("로그인 실패");
        }
        return response.json();
    })
    .then(data => {
        localStorage.setItem('jwt', data.token);
    })
}

function getJwt() {
    return localStorage.getItem('jwt');
}

function logout() {
    localStorage.removeItem('jwt');
    alert('성공적으로 로그아웃되셨습니다.');
    window.location.href = '/'
}
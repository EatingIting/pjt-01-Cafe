const LIKE_KEY = "likedMenus";

document.addEventListener("DOMContentLoaded", () => {
    // ... (생략: 좋아요 상태 불러오는 기존 로직 유지)
    const likedMenus = JSON.parse(localStorage.getItem(LIKE_KEY)) || [];
    const likeButtons = document.querySelectorAll(".menu-item .like-button");

    likeButtons.forEach((btn) => {
        const menuItem = btn.closest(".menu-item");
        const menuName = menuItem.querySelector(".menu-name").textContent.trim();
        const isLiked = likedMenus.some(item => item.name === menuName); // 이름으로 체크

        if (isLiked) {
            btn.textContent = "❤";
        }
    });
});

// HTML 요소에서 메뉴 상세 정보를 추출합니다.
function getMenuDetails(element) {
    const menuItem = element.closest(".menu-item");
    const name = menuItem.querySelector(".menu-name").textContent.trim();

    // th:data-price 속성에서 가격을 가져옵니다. (HTML을 수정했을 경우)
    const priceText = menuItem.querySelector(".menu-price").dataset.price ||
        menuItem.querySelector(".menu-price").textContent.replace('원', '').trim();
    const price = parseInt(priceText, 10);

    // 이미지 경로를 가져옵니다.
    const image = menuItem.querySelector(".menu-image").getAttribute('src');

    return { name, price, image, temp: 'ICE/HOT' }; // mypick에서 사용할 수 있도록 필수 정보 반환
}


function toggleLike(element, event) {
    event.stopPropagation();
    const menuDetails = getMenuDetails(element);
    let likedMenus = JSON.parse(localStorage.getItem(LIKE_KEY)) || [];

    const isLiked = likedMenus.some(item => item.name === menuDetails.name);

    if (isLiked) {
        likedMenus = likedMenus.filter((item) => item.name !== menuDetails.name);
        element.textContent = "♡";
        console.log(menuDetails.name + " 좋아요 해제");
    } else {
        likedMenus.push(menuDetails);
        element.textContent = "❤";
        console.log(menuDetails.name + " 좋아요 설정");
    }

    localStorage.setItem(LIKE_KEY, JSON.stringify(likedMenus));
}

function selectMenu(menuName) {
    console.log(menuName + ' 선택됨');
}
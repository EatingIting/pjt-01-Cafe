const LIKE_KEY = "likedMenus";

document.addEventListener("DOMContentLoaded", () => {
    const likedMenus = JSON.parse(localStorage.getItem(LIKE_KEY)) || [];
    const likeButtons = document.querySelectorAll(".menu-item .like-button");

    likeButtons.forEach((btn) => {
        const menuName = btn.parentElement.querySelector(".menu-name").textContent.trim();
        if (likedMenus.includes(menuName)) {
            btn.textContent = "❤";
        }
    });
});

function toggleLike(element, event) {
    event.stopPropagation();
    const menuName = element.parentElement.querySelector(".menu-name").textContent.trim();
    let likedMenus = JSON.parse(localStorage.getItem(LIKE_KEY)) || [];

    const isLiked = likedMenus.includes(menuName);

    if (isLiked) {
        likedMenus = likedMenus.filter((name) => name !== menuName);
        element.textContent = "♡";
        console.log(menuName + " 좋아요 해제");
    } else {
        likedMenus.push(menuName);
        element.textContent = "❤";
        console.log(menuName + " 좋아요 설정");
    }

    localStorage.setItem(LIKE_KEY, JSON.stringify(likedMenus));
}

function selectMenu(menuName) {
    console.log(menuName + ' 선택됨');
}
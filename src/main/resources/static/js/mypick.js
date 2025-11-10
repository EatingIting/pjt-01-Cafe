const LIKE_KEY = "likedMenus";

// 메뉴 데이터 예시
const MENU_DATA = {
    "아메리카노": { price: 3000, image: "img/아아.png" },
    "바닐라라떼": { price: 3000, image: "img/아바라.png" },
    "밤라떼": { price: 3500, image: "img/밤라떼.jpg" },
    "군고구마라떼": { price: 3500, image: "img/군고구마라떼.png" }
};

document.addEventListener("DOMContentLoaded", () => {
    const likedMenus = JSON.parse(localStorage.getItem(LIKE_KEY)) || [];
    const listContainer = document.getElementById("myPickList");

    if (likedMenus.length === 0) {
        listContainer.innerHTML = `
          <p style="text-align:center; color:#777; font-size:16px; margin-top:40px;">
            아직 원픽 메뉴가 없습니다.<br>하트를 눌러 나만의 메뉴를 추가해보세요!
          </p>`;
        return;
    }

    likedMenus.forEach(menuName => {
        const menu = MENU_DATA[menuName];
        if (!menu) return;

        const item = document.createElement("div");
        item.classList.add("menu-item");

        item.innerHTML = `
          <img src="${menu.image}" alt="${menuName}" class="menu-image">
          <div class="menu-details">
              <div class="menu-name">${menuName}</div>
              <div>
                <span class="menu-temp">ICE</span>
                <span class="menu-temp hot">HOT</span>
              </div>
              <div class="menu-price">${menu.price}원~</div>
          </div>
          <span class="like-button" onclick="removeLike('${menuName}', this)">❤</span>
        `;

        listContainer.appendChild(item);
    });
});

function removeLike(menuName, element) {
    let likedMenus = JSON.parse(localStorage.getItem(LIKE_KEY)) || [];
    likedMenus = likedMenus.filter(name => name !== menuName);
    localStorage.setItem(LIKE_KEY, JSON.stringify(likedMenus));
    element.closest(".menu-item").remove();
}
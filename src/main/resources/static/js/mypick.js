const LIKE_KEY = "likedMenus";

document.addEventListener("DOMContentLoaded", () => {
    const likedMenus = JSON.parse(localStorage.getItem(LIKE_KEY)) || [];
    const listContainer = document.getElementById("myPickList");

    if (!likedMenus || likedMenus.length === 0) {
        listContainer.innerHTML = `
          <p style="text-align:center; color:#777; font-size:16px; margin-top:40px;">
            아직 원픽 메뉴가 없습니다.<br>하트를 눌러 나만의 메뉴를 추가해보세요!
          </p>`;
        return;
    }

    likedMenus.forEach(menu => {
        const item = document.createElement("div");
        item.classList.add("menu-item");
        item.dataset.menuId = menu.menuId; // menuId 저장

        item.innerHTML = `
          <img src="${menu.image}" alt="${menu.name}" class="menu-image">
          <div class="menu-details">
              <div class="menu-name">${menu.name}</div>
              <div>
                  <span class="menu-temp">ICE</span>
                  <span class="menu-temp hot">HOT</span>
              </div>
              <div class="menu-price" data-price="${menu.price}">${menu.price}원</div>
          </div>
          <span class="like-button" onclick="removeLike('${menu.menuId}', this)">❤</span>
        `;

        listContainer.appendChild(item);
    });
});

// 좋아요 해제
function removeLike(menuId, element) {
    let likedMenus = JSON.parse(localStorage.getItem(LIKE_KEY)) || [];
    likedMenus = likedMenus.filter(item => item.menuId !== menuId);
    localStorage.setItem(LIKE_KEY, JSON.stringify(likedMenus));

    // UI에서 즉시 제거
    const menuItem = element.closest(".menu-item");
    if (menuItem) {
        menuItem.remove();
    }
}

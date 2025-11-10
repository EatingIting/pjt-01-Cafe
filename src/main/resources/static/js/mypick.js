const LIKE_KEY = "likedMenus";

// ⚠️ MENU_DATA 객체를 제거합니다. ⚠️

document.addEventListener("DOMContentLoaded", () => {
    // likedMenus는 이제 메뉴 상세 정보 객체의 배열입니다.
    const likedMenus = JSON.parse(localStorage.getItem(LIKE_KEY)) || [];
    const listContainer = document.getElementById("myPickList");

    if (likedMenus.length === 0) {
        listContainer.innerHTML = `
          <p style="text-align:center; color:#777; font-size:16px; margin-top:40px;">
            아직 원픽 메뉴가 없습니다.<br>하트를 눌러 나만의 메뉴를 추가해보세요!
          </p>`;
        return;
    }

    // likedMenus 배열의 각 객체(menu)를 사용합니다.
    likedMenus.forEach(menu => {
        const item = document.createElement("div");
        item.classList.add("menu-item");

        item.innerHTML = `
          <img src="${menu.image}" alt="${menu.name}" class="menu-image">
          <div class="menu-details">
              <div class="menu-name">${menu.name}</div>
              <div>
                <span class="menu-temp">ICE</span>
                <span class="menu-temp hot">HOT</span>
              </div>
              <div class="menu-price">${menu.price}원~</div> 
          </div>
          <span class="like-button" onclick="removeLike('${menu.name}', this)">❤</span>
        `;

        listContainer.appendChild(item);
    });
});

function removeLike(menuName, element) {
    let likedMenus = JSON.parse(localStorage.getItem(LIKE_KEY)) || [];
    // 메뉴 객체 배열에서 이름이 일치하는 항목을 제거
    likedMenus = likedMenus.filter(item => item.name !== menuName);
    localStorage.setItem(LIKE_KEY, JSON.stringify(likedMenus));
    element.closest(".menu-item").remove();
}
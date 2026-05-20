import os
import glob

html_files = glob.glob('c:/Users/Bach/Desktop/TTCS/QuanLyDaoTao-main/QuanLyDaoTao-main/QuanLyDaoTao/src/main/resources/static/*.html')

topbar_html = """<style>
.topbar { display: flex; justify-content: flex-end; align-items: center; padding: 10px 25px; background: white; border-bottom: 1px solid #ddd; margin: -25px -25px 25px -25px; }
.user-profile { position: relative; cursor: pointer; display: flex; align-items: center; gap: 10px; }
.user-profile img { width: 40px; height: 40px; border-radius: 50%; }
.user-info-top { text-align: right; }
.user-info-top .name { font-weight: bold; font-size: 14px; margin-bottom: 2px; color: #333;}
.user-info-top .msv { font-size: 12px; color: #666; }
.dropdown-menu { display: none; position: absolute; top: 100%; right: 0; background: white; box-shadow: 0 4px 12px rgba(0,0,0,0.15); border-radius: 8px; width: 230px; z-index: 1000; padding: 10px 0; margin-top: 10px; }
.dropdown-menu.show { display: block; }
.dropdown-menu li { list-style: none; padding: 12px 20px; border-bottom: 1px solid #eee; font-size: 14px; color: #333; }
.dropdown-menu li:last-child { border-bottom: none; }
.dropdown-menu li a { text-decoration: none; color: #333; }
.dropdown-menu li:hover { background: #f5f5f5; }
</style>
<div class="main-content">
  <div class="topbar">
      <div class="user-profile" onclick="document.getElementById('user-dropdown').classList.toggle('show')">
          <div class="user-info-top">
              <div class="name" id="topbar-name">Đang tải...</div>
              <div class="msv" id="topbar-msv">---</div>
          </div>
          <img src="https://ui-avatars.com/api/?name=U&background=bb0000&color=fff" id="topbar-avatar" alt="avatar">
          <ul class="dropdown-menu" id="user-dropdown">
              <li><i class="fa-solid fa-address-card"></i> Họ tên: <b id="drop-name">...</b></li>
              <li><i class="fa-solid fa-id-badge"></i> Tài khoản: <b id="drop-msv">...</b></li>
              <li style="cursor: pointer;" onclick="location.href='thong-tin.html'"><i class="fa-solid fa-user-gear"></i> Thông tin cá nhân</li>
              <li style="color: red; cursor: pointer;" onclick="localStorage.clear(); location.href='login.html'"><i class="fa-solid fa-right-from-bracket"></i> Đăng xuất</li>
          </ul>
      </div>
  </div>
  <script>
      document.addEventListener('DOMContentLoaded', function() {
          const topMaSV = localStorage.getItem('maSVHienTai');
          if(topMaSV) {
              fetch('http://localhost:8080/api/sinhvien/' + topMaSV)
              .then(res => res.json())
              .then(data => {
                  document.getElementById('topbar-name').innerText = data.hoTen;
                  document.getElementById('topbar-msv').innerText = data.maSV;
                  document.getElementById('drop-name').innerText = data.hoTen;
                  document.getElementById('drop-msv').innerText = data.maSV;
                  document.getElementById('topbar-avatar').src = 'https://ui-avatars.com/api/?name=' + encodeURIComponent(data.hoTen) + '&background=bb0000&color=fff';
              }).catch(e => console.log(e));
          }
          document.addEventListener('click', function(event) {
              const profile = document.querySelector('.user-profile');
              if (profile && !profile.contains(event.target)) {
                  const dropdown = document.getElementById('user-dropdown');
                  if(dropdown) dropdown.classList.remove('show');
              }
          });
      });
  </script>"""

for filepath in html_files:
    if 'login.html' in filepath:
        continue
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    if 'class="topbar"' in content:
        continue # Already injected
        
    # Replace the first <div class="main-content">
    new_content = content.replace('<div class="main-content">', topbar_html, 1)
    
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(new_content)
print('Injected topbar into all HTML files successfully!')

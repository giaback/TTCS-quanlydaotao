import glob
import os

html_files = glob.glob('c:/Users/Bach/Desktop/TTCS/QuanLyDaoTao-main/QuanLyDaoTao-main/QuanLyDaoTao/src/main/resources/static/*.html')

lines_to_remove = [
    '<li onclick="location.href=\'thong-tin.html\'"><i class="fa-solid fa-user-gear"></i> Thông tin cá nhân</li>',
    '<li class="active" onclick="location.href=\'thong-tin.html\'"><i class="fa-solid fa-user-gear"></i> Thông tin cá nhân</li>'
]

for filepath in html_files:
    if 'login.html' in filepath:
        continue
    with open(filepath, 'r', encoding='utf-8') as f:
        lines = f.readlines()
    
    new_lines = []
    for line in lines:
        should_remove = False
        for target in lines_to_remove:
            if target in line:
                should_remove = True
                break
        if not should_remove:
            new_lines.append(line)
            
    with open(filepath, 'w', encoding='utf-8') as f:
        f.writelines(new_lines)

print("Removed 'Thông tin cá nhân' from sidebars successfully!")

import requests

try:
    url = input("请输入网址: ")
    filename = "downloaded.html"
    response = requests.get(url)
    # 保存为HTML文件
    with open(filename, 'w', encoding=response.encoding or 'utf-8') as f:
        f.write(response.text)

    size_bytes = len(response.content)
    size_kb = size_bytes / 1024

    print(f"网页已保存为 {filename}")
    print(f"网页大小: {size_bytes} 字节 ({size_kb:.5f} KB)")

except requests.exceptions.RequestException as e:
    print(f"请求失败: {e}")

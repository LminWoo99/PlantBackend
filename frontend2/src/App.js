import React, { useState } from "react";
import axios from "axios";
import './App.css';

const App = () => {
  const [inputTitle, setInputTitle] = useState("");
  const [inputContent, setInputContent] = useState("");

  const handleTitleChange = (event) => {
    setInputTitle(event.target.value);
  };

  const handleContentChange = (event) => {
    setInputContent(event.target.value);
  };

  const handleCreateSubmit = (event) => {
    event.preventDefault();

    if (!inputTitle || !inputContent) {
      return;
    }

    const newPost = {
      tTitle: inputTitle,
      tContent: inputContent,
      writer: "작성자",
      // 필요한 경우 더 추가
    };
    axios.post("/post", newPost)
      .then(response => {
        console.log(response.data);
        setInputTitle("");
        setInputContent("");
        // 글 작성 성공 시 처리할 코드 작성
      })
      .catch(error => console.log(error));
  };

  const handleEditSubmit = (id) => (event) => {
    event.preventDefault();

    if (!inputTitle || !inputContent) {
      return;
    }

    const updatedPost = {
      tTitle: inputTitle,
      tContent: inputContent,
      // 필요한 경우 더 추가
    };
    axios.put(`/post/list/${id}`, updatedPost)
      .then(response => {
        console.log(response.data);
        setInputTitle("");
        setInputContent("");
        // 글 수정 성공 시 처리할 코드 작성
      })
      .catch(error => console.log(error));
  };

  const handleDelete = (id) => {
    axios.delete(`/post/list/${id}`)
      .then(response => {
        console.log(response.data);
        // 글 삭제 성공 시 처리할 코드 작성
      })
      .catch(error => console.log(error));
  };

  return (
    <div>
      <form onSubmit={handleCreateSubmit}>
        <div>
          <h1>글 작성</h1>
        </div>
        <div>
          <label>제목:</label>
          <input name="tTitle" value={inputTitle} onChange={handleTitleChange} />
        </div>
        <div>
          <label>내용:</label>
          <textarea name="tContent" value={inputContent} onChange={handleContentChange}></textarea>
        </div>
        <button type="submit">작성</button>
      </form>
      {/* 글 목록이나 상세보기 등을 구현할 수 있음 */}
      <div>
        <h1>글 수정/삭제</h1>
        <ul>
          <li>
            <form onSubmit={handleEditSubmit(1)}>
              <div>
                <label>제목:</label>
                <input name="tTitle" value={inputTitle} onChange={handleTitleChange} />
              </div>
              <div>
                <label>내용:</label>
                <textarea name="tContent" value={inputContent} onChange={handleContentChange}></textarea>
              </div>
              </form>
              </li>
              </ul>
              </div>
              </div>
              );
              }
export default App

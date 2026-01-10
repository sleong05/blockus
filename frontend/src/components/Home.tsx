import { MouseEventHandler } from "react";
import { useNavigate } from "react-router-dom"

function Home() {
    const navigate = useNavigate();

    const createGame = async () => {
        const response = await fetch(`${import.meta.env.VITE_BACKEND_API}/api/games`, {
            method: 'POST'
        });

        const game = await response.json();

        navigate(`/game/${game.id}`);
    }


    return (
        <>
            <div className="home">
                <h2>Welcome to Blockus</h2>
                <button onClick={createGame}>Create Game</button>
            </div>
        </>
    )
}

export default Home
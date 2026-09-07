{
  description = "ivr api for banking service flake";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixos-26.05";
  };

  outputs = {nixpkgs, ...}: let
    system = "x86_64-linux";
    pkgs = import nixpkgs {inherit system; };
  in {
    devShells.${system}.default = pkgs.mkShell {
      packages = with pkgs; [
        # insert desired packages
      ];

      shellHook = ''
        echo "cleaning .env file"
        sed -i '/^[[:space:]]*$/N;/^\n$/D' .env

        echo "load .env file"
        if [ -f .env ]; then
            set -a
            source .env
            set +a
            echo ".env file loaded successfully!"
        else
            echo "Could not load .env file. File not found"
        fi
        echo "dev env is fully set!"
      '';
    };
  };
}

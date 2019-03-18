# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|
    config.vm.box = "codeyourinfra/docker"
    config.vm.network "private_network", ip: "192.168.33.10"
    config.vm.synced_folder ".", "/vagrant", SharedFoldersEnableSymlinksCreate: false
  
    config.vm.provider "virtualbox" do |vb|
      vb.memory = "2048"
    end
  
    config.vm.provision "ansible" do |ansible|
      ansible.playbook = "playbook.yml"
      ansible.inventory_path = "inventory.yml"
    end
  end
  